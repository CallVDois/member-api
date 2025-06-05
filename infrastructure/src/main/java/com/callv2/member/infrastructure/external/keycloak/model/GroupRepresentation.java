package com.callv2.member.infrastructure.external.keycloak.model;

import java.util.List;
import java.util.Map;

public record GroupRepresentation(
        String id,
        String name,
        String description,
        String path,
        String parentId,
        Long subGroupCount,
        List<GroupRepresentation> subGroups,
        Map<String, List<String>> attributes,
        List<String> realmRoles,
        Map<String, List<String>> clientRoles,
        Map<String, Boolean> access) {

}
