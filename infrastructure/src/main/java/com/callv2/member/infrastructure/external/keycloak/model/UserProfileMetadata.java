package com.callv2.member.infrastructure.external.keycloak.model;

import java.util.List;

public record UserProfileMetadata(
        List<UserProfileAttributeMetadata> attributes,
        List<UserProfileAttributeGroupMetadata> groups) {

}
