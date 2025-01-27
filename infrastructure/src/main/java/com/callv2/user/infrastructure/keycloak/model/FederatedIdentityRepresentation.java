package com.callv2.user.infrastructure.keycloak.model;

public record FederatedIdentityRepresentation(
        String identityProvider,
        String userId,
        String userName) {

}
