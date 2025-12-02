package com.asrevo.cvhome.uaa.dto;

import com.asrevo.cvhome.uaa.service.ClientAuthMethod;
import com.asrevo.cvhome.uaa.service.OAuthGrantType;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

public record ClientDetails(String clientId,
                            Instant clientIdIssuedAt,
                            Instant clientSecretExpiresAt,
                            String clientName,
                            Set<ClientAuthMethod> clientAuthenticationMethods,
                            Set<OAuthGrantType> authorizationGrantTypes,
                            Set<String> redirectUris,
                            Set<String> postLogoutRedirectUris,
                            Set<String> scopes,
                            ClientDetailsSettings clientSettings,
                            ClientDetailsTokens tokenSettings
) {
}




