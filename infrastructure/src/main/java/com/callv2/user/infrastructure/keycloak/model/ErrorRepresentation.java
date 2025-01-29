package com.callv2.user.infrastructure.keycloak.model;

import java.util.List;

public record ErrorRepresentation(
        String field,
        String errorMessage,
        Object[] params,
        List<ErrorRepresentation> errors) {

}
