package com.callv2.member.infrastructure.external.keycloak.model;

public record FederatedIdentityRepresentation(
        String identityProvider,
        String userId,
        String userName) {

}
