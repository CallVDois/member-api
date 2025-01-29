package com.callv2.user.infrastructure.keycloak.exception;

import java.util.function.Predicate;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class InternalServerError extends KeycloakException {

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    private InternalServerError(final String message) {
        super(message);
    }

    public static InternalServerError from(final String message) {
        return new InternalServerError(message);
    }

    public static Predicate<HttpStatusCode> isSame() {
        return STATUS::isSameCodeAs;
    }

    public HttpStatus status() {
        return STATUS;
    }

}
