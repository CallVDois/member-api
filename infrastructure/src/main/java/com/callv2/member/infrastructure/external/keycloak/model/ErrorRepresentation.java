package com.callv2.member.infrastructure.external.keycloak.model;

import java.util.List;

public record ErrorRepresentation(
        String field,
        String errorMessage,
        Object[] params,
        List<ErrorRepresentation> errors) {

}
