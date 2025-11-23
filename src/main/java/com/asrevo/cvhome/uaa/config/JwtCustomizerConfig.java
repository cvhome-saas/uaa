package com.asrevo.cvhome.uaa.config;

import com.asrevo.cvhome.uaa.domain.Role;
import com.asrevo.cvhome.uaa.domain.User;
import com.asrevo.cvhome.uaa.repo.UserRepository;
import com.asrevo.cvhome.uaa.service.ClientRoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class JwtCustomizerConfig {

    private final UserRepository users;
    private final ClientRoleService clientRoles;

    public JwtCustomizerConfig(UserRepository users, ClientRoleService clientRoles) {
        this.users = users;
        this.clientRoles = clientRoles;
    }

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> oauth2TokenCustomizer() {
        return context -> {
            // Only enrich access tokens
            if (!"access_token".equals(context.getTokenType().getValue())) {
                return;
            }

            // Machine-to-machine: client_credentials (no end-user)
            if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType())) {
                String clientId = context.getRegisteredClient() != null ? context.getRegisteredClient().getClientId() : null;
                if (clientId != null) {
                    Set<String> roles = clientRoles.getClientRoleNames(clientId);
                    if (!roles.isEmpty()) {
                        context.getClaims().claim("roles", roles);
                    }
                    Set<String> perms = clientRoles.getClientPermissionNames(clientId);
                    if (!perms.isEmpty()) {
                        context.getClaims().claim("perms", perms);
                    }
                    // make client_id explicit for resource servers
                    context.getClaims().claim("client_id", clientId);
                }
                return;
            }

            Authentication principal = context.getPrincipal();
            if (principal == null) return;

            String username = principal.getName();
            users.findByUsername(username).ifPresent(user -> addUserClaims(context, user));
        };
    }

    private void addUserClaims(JwtEncodingContext context, User user) {
        // roles
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        if (!roles.isEmpty()) {
            context.getClaims().claim("roles", roles);
        }
        // permissions aggregated from roles
        Set<String> perms = user.getRoles().stream()
            .flatMap(r -> r.getPermissions().stream())
            .map(p -> p.getName())
            .collect(Collectors.toSet());
        if (!perms.isEmpty()) {
            context.getClaims().claim("perms", perms);
        }
    }
}
