package com.callv2.member.infrastructure.keycloak.model;

public record CredentialRepresentation(
        String id,
        String type,
        String userLabel,
        Long createdDate,
        String secretData,
        String credentialData,
        Integer priority,
        String value,
        Boolean temporary,
        String device,
        String hashedSaltedValue,
        String salt,
        Integer hashIterations,
        Integer counter,
        String algorithm,
        Integer digits,
        Integer period,
        AllOfCredentialRepresentationConfig config) {

}
