package com.callv2.member.infrastructure.keycloak.model;

import java.util.Map;

public record UserProfileAttributeGroupMetadata(
        String name,
        String displayHeader,
        String displayDescription,
        Map<String, Object> annotations) {

}
