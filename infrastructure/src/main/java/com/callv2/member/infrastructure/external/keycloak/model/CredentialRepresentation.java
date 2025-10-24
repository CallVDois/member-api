package com.callv2.member.infrastructure.external.keycloak.model;

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

    @Override
    public String toString() {
        return "CredentialRepresentation [id=" + id + ", type=" + type + ", userLabel=" + userLabel + ", createdDate="
                + createdDate + ", secretData=" + secretData + ", credentialData=" + credentialData + ", priority="
                + priority + ", value=" + "****" + ", temporary=" + temporary + ", device=" + device
                + ", hashedSaltedValue=" + hashedSaltedValue + ", salt=" + salt + ", hashIterations="
                + hashIterations + ", counter=" + counter + ", algorithm=" + algorithm + ", digits=" + digits
                + ", period=" + period + ", config=" + config + "]";
    }

}
