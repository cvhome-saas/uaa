package com.asrevo.cvhome.uaa.mapper;

import com.asrevo.cvhome.uaa.dto.ClientDetails;
import com.asrevo.cvhome.uaa.dto.ClientDetailsSettings;
import com.asrevo.cvhome.uaa.dto.ClientDetailsTokens;
import com.asrevo.cvhome.uaa.service.ClientAuthMethod;
import com.asrevo.cvhome.uaa.service.OAuthGrantType;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientClientDetailsMapper {
    
    public static ClientDetails toClientDetails(RegisteredClient client) {
        Set<ClientAuthMethod> clientAuthenticationMethods = client.getClientAuthenticationMethods()
                .stream()
                .map(ClientAuthenticationMethod::getValue)
                .map(ClientAuthMethod::from)
                .collect(Collectors.toSet());
        Set<OAuthGrantType> authorizationGrantTypes = client.getAuthorizationGrantTypes()
                .stream()
                .map(AuthorizationGrantType::getValue)
                .map(OAuthGrantType::from)
                .collect(Collectors.toSet());
        return new ClientDetails(client.getId(),
                client.getClientId(),
                client.getClientIdIssuedAt(),
                client.getClientSecretExpiresAt(),
                client.getClientName(),
                clientAuthenticationMethods,
                authorizationGrantTypes,
                client.getRedirectUris(),
                client.getPostLogoutRedirectUris(),
                client.getScopes(),
                toClientSetting(client.getClientSettings()),
                toTokenSetting(client.getTokenSettings()));
    }

    private static ClientDetailsTokens toTokenSetting(TokenSettings settings) {
        return new ClientDetailsTokens(
                settings.getAuthorizationCodeTimeToLive(),
                settings.getAccessTokenTimeToLive(),
                settings.getAccessTokenFormat(),
                settings.getDeviceCodeTimeToLive(),
                settings.isReuseRefreshTokens(),
                settings.getRefreshTokenTimeToLive(),
                settings.getIdTokenSignatureAlgorithm(),
                settings.isX509CertificateBoundAccessTokens()
        );
    }

    private static ClientDetailsSettings toClientSetting(ClientSettings settings) {
        return new ClientDetailsSettings(
                settings.isRequireProofKey(),
                settings.isRequireAuthorizationConsent(),
                settings.getJwkSetUrl(),
                settings.getTokenEndpointAuthenticationSigningAlgorithm(),
                settings.getX509CertificateSubjectDN()
        );
    }

    public static RegisteredClient toRegisteredClient(ClientDetails details) {
        TokenSettings tokenSettings = TokenSettings.builder().build();
        if (details.tokenSettings() != null) {
            tokenSettings = TokenSettings
                    .builder()
                    .authorizationCodeTimeToLive(details.tokenSettings().authorizationCodeTimeToLive())
                    .accessTokenTimeToLive(details.tokenSettings().accessTokenTimeToLive())
                    .accessTokenFormat(details.tokenSettings().accessTokenFormat())
                    .deviceCodeTimeToLive(details.tokenSettings().deviceCodeTimeToLive())
                    .reuseRefreshTokens(details.tokenSettings().reuseRefreshTokens())
                    .refreshTokenTimeToLive(details.tokenSettings().refreshTokenTimeToLive())
                    .idTokenSignatureAlgorithm(details.tokenSettings().idTokenSignatureAlgorithm())
                    .x509CertificateBoundAccessTokens(details.tokenSettings().x509CertificateBoundAccessTokens())
                    .build();
        }
        ClientSettings clientSettings = ClientSettings.builder().build();
        if (details.clientSettings() != null) {
            clientSettings = ClientSettings
                    .builder()
                    .requireProofKey(details.clientSettings().requireProofKey())
                    .requireAuthorizationConsent(details.clientSettings().requireAuthorizationConsent())
                    .jwkSetUrl(details.clientSettings().jwkSetUrl())
                    .tokenEndpointAuthenticationSigningAlgorithm(details.clientSettings().tokenEndpointAuthenticationSigningAlgorithm())
                    .x509CertificateSubjectDN(details.clientSettings().x509CertificateSubjectDN())
                    .build();
        }
        return RegisteredClient.withId(details.id())
                .clientId(details.clientId())
                .clientName(details.clientName())
                .clientAuthenticationMethods(clientAuthenticationMethods -> {
                    if (details.clientAuthenticationMethods() != null) {
                        Set<ClientAuthenticationMethod> newClientAuthenticationMethods = details.clientAuthenticationMethods()
                                .stream()
                                .map(ClientAuthMethod::value)
                                .map(ClientAuthenticationMethod::valueOf)
                                .collect(Collectors.toSet());
                        clientAuthenticationMethods.addAll(newClientAuthenticationMethods);
                    }
                })
                .authorizationGrantTypes(authorizationGrantTypes -> {
                    if (details.authorizationGrantTypes() != null) {
                        Set<AuthorizationGrantType> newAuthorizationGrantTypes = details.authorizationGrantTypes()
                                .stream()
                                .map(OAuthGrantType::value)
                                .map(AuthorizationGrantType::new)
                                .collect(Collectors.toSet());
                        authorizationGrantTypes.addAll(newAuthorizationGrantTypes);
                    }
                })
                .redirectUris(redirectUris -> {
                    if (details.redirectUris() != null) {
                        redirectUris.addAll(details.redirectUris());
                    }
                })
                .postLogoutRedirectUris(postLogoutRedirectUris -> {
                    if (details.postLogoutRedirectUris() != null) {
                        postLogoutRedirectUris.addAll(details.postLogoutRedirectUris());
                    }
                })
                .scopes(scopes -> {
                    if (details.scopes() != null) {
                        scopes.addAll(details.scopes());
                    }
                })
                .tokenSettings(tokenSettings)
                .clientSettings(clientSettings)
                .build();
    }
}
