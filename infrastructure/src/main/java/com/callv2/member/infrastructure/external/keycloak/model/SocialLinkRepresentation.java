package com.callv2.member.infrastructure.external.keycloak.model;

public record SocialLinkRepresentation(
        String socialProvider,
        String socialUserId,
        String socialUsername) {

}
