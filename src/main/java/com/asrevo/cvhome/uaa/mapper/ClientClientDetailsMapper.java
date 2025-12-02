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

import java.util.Map;
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
        return new ClientDetails(client.getClientId(),
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
}
