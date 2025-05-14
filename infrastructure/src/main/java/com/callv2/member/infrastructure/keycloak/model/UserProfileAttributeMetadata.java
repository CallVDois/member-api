package com.callv2.member.infrastructure.keycloak.model;

import java.util.Map;

public record UserProfileAttributeMetadata(
        String name,
        String displayName,
        Boolean required,
        Boolean readOnly,
        Map<String, Object> annotations,
        Map<String, Map<String, Object>> validators,
        String group,
        Boolean multivalued) {

}
