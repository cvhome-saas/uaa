package com.asrevo.cvhome.uaa.dto;

import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;

public record ClientDetailsSettings(boolean requireProofKey,
                                    boolean requireAuthorizationConsent,
                                    String jwkSetUrl,
                                    JwsAlgorithm tokenEndpointAuthenticationSigningAlgorithm,
                                    String x509CertificateSubjectDN) {

}
