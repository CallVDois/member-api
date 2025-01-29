package com.callv2.user.infrastructure.keycloak.model;

import java.util.List;

public record UserProfileMetadata(
        List<UserProfileAttributeMetadata> attributes,
        List<UserProfileAttributeGroupMetadata> groups) {

}
