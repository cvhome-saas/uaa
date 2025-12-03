package com.asrevo.cvhome.uaa.service;

import com.asrevo.cvhome.uaa.dto.ClientDetails;
import com.asrevo.cvhome.uaa.dto.ClientSummary;
import com.asrevo.cvhome.uaa.dto.CreateClientCommand;
import com.asrevo.cvhome.uaa.dto.CreatedClient;
import com.asrevo.cvhome.uaa.mapper.ClientClientDetailsMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class AdminClientService {


    private final RegisteredClientRepository clients;
    private final PasswordEncoder encoder;
    private final JdbcTemplate jdbc;

    public AdminClientService(RegisteredClientRepository clients, PasswordEncoder encoder, JdbcTemplate jdbc) {
        this.clients = clients;
        this.encoder = encoder;
        this.jdbc = jdbc;
    }

    public CreatedClient createClient(CreateClientCommand cmd) {
        // Use standards-based defaults when not provided
        Set<OAuthGrantType> grantTypes = cmd.grantTypes();
        Set<ClientAuthMethod> authMethods = cmd.authMethods();
        Boolean requirePkce = cmd.requirePkce();
        Boolean requireConsent = cmd.requireConsent();

        if (grantTypes == null || grantTypes.isEmpty()) {
            grantTypes = Set.of(OAuthGrantType.AUTHORIZATION_CODE, OAuthGrantType.REFRESH_TOKEN);
        }
        if (authMethods == null || authMethods.isEmpty()) {
            authMethods = Set.of(ClientAuthMethod.CLIENT_SECRET_BASIC);
        }
        if (requirePkce == null) requirePkce = true;
        if (requireConsent == null) requireConsent = true;

        if (cmd.clientName() == null || cmd.clientName().isBlank()) {
            throw new IllegalArgumentException("clientName is required");
        }

        String clientId = cmd.clientId() != null && !cmd.clientId().isBlank() ? cmd.clientId() : generateReadableId();

        String rawSecret = null;
        RegisteredClient.Builder b = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId(clientId)
            .clientName(cmd.clientName());

        // Auth methods
        boolean needsSecret = false;
        for (ClientAuthMethod am : authMethods) {
            ClientAuthenticationMethod method = am.toSpring();
            b.clientAuthenticationMethod(method);
            if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.equals(method)
                || ClientAuthenticationMethod.CLIENT_SECRET_POST.equals(method)) {
                needsSecret = true;
            }
        }
        if (needsSecret) {
            rawSecret = generateSecret();
            b.clientSecret(encoder.encode(rawSecret));
        }

        // Grant types
        boolean hasAuthCode = false;
        boolean hasRefresh = false;
        boolean hasClientCreds = false;
        for (OAuthGrantType gt : grantTypes) {
            b.authorizationGrantType(gt.toSpring());
            switch (gt) {
                case AUTHORIZATION_CODE -> hasAuthCode = true;
                case REFRESH_TOKEN -> hasRefresh = true;
                case CLIENT_CREDENTIALS -> hasClientCreds = true;
            }
        }

        TokenSettings.Builder ts = TokenSettings.builder();
        if (hasClientCreds) {
            ts.accessTokenTimeToLive(Duration.ofMinutes(5));
        }
        if (hasAuthCode || hasRefresh) {
            ts.accessTokenTimeToLive(Duration.ofMinutes(10));
            if (hasRefresh) ts.reuseRefreshTokens(false);
        }
        b.tokenSettings(ts.build());

        // Defer building client settings until after redirect/public client enforcement
        ClientSettings.Builder cs = ClientSettings.builder();

        // Scopes
        Set<String> defaultScopes = (hasClientCreds && !(hasAuthCode || hasRefresh))
            ? Set.of("api.read", "api.write")
            : Set.of(OidcScopes.OPENID, "profile");
        scopesOrDefault(cmd.scopes(), defaultScopes).stream().distinct().forEach(b::scope);

        // Redirect URIs for code flow
        if (hasAuthCode) {
            if (cmd.redirectUris() == null || cmd.redirectUris().isEmpty()) {
                throw new IllegalArgumentException("redirectUris are required when using authorization_code grant");
            }
            cmd.redirectUris().forEach(b::redirectUri);
        }

        // Apply client settings once with correct PKCE/consent values
        boolean finalRequirePkce = (hasAuthCode && !needsSecret) || requirePkce;
        cs.requireProofKey(finalRequirePkce);
        cs.requireAuthorizationConsent(requireConsent);
        b.clientSettings(cs.build());

        RegisteredClient rc = b.build();
        clients.save(rc);
        return new CreatedClient(clientId, rawSecret);
    }

    private Set<String> scopesOrDefault(Set<String> scopes, Set<String> defaults) {
        return (scopes == null || scopes.isEmpty()) ? defaults : scopes;
    }

    private String generateSecret() {
        byte[] bytes = new byte[48];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String generateReadableId() {
        return "client-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    public Page<ClientSummary> listClients(Pageable pageable) {

        Long total = jdbc.queryForObject("select count(*) from oauth2_registered_client", Long.class);
        var items = jdbc.query(
            "select id, client_id, client_name from oauth2_registered_client order by id limit ? offset ?",
            (rs, rowNum) -> new ClientSummary(rs.getString(1), rs.getString(2), rs.getString(3)), pageable.getPageSize(), pageable.getOffset());

        return new PageImpl<>(items, pageable, total);
    }

    public boolean delete(String id) {
        int updatedRows = jdbc.update("delete from oauth2_registered_client where id=?", id);
        return updatedRows > 0;
    }

    public ClientDetails findById(String id) {
        return ClientClientDetailsMapper.toClientDetails(Objects.requireNonNull(this.clients.findById(id)));
    }
    
    public void save(ClientDetails details){
        clients.save(ClientClientDetailsMapper.toRegisteredClient(details));
    }
}
