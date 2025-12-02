package com.asrevo.cvhome.uaa.dto;

import com.asrevo.cvhome.uaa.service.ClientAuthMethod;
import com.asrevo.cvhome.uaa.service.OAuthGrantType;

import java.util.Set;

public record CreateClientCommand(
        String clientId,
        String clientName,
        Set<String> redirectUris,
        Set<String> scopes,
        Set<OAuthGrantType> grantTypes,      // standardized enum grant types
        Set<ClientAuthMethod> authMethods,     // standardized enum client auth methods
        Boolean requirePkce,
        Boolean requireConsent
) {
}
