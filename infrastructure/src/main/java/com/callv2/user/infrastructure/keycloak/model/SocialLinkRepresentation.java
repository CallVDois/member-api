package com.callv2.user.infrastructure.keycloak.model;

public record SocialLinkRepresentation(
        String socialProvider,
        String socialUserId,
        String socialUsername) {

}
