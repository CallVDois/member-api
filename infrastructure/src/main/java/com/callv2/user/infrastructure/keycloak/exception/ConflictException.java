package com.callv2.user.infrastructure.keycloak.exception;

import java.util.function.Predicate;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.callv2.user.infrastructure.keycloak.model.ErrorRepresentation;

public class ConflictException extends KeycloakException {

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    private ConflictException(final ErrorRepresentation error) {
        super(error.errorMessage());
    }

    public static ConflictException from(final ErrorRepresentation error) {
        return new ConflictException(error);
    }

    public static Predicate<HttpStatusCode> isSame() {
        return STATUS::isSameCodeAs;
    }

    public HttpStatus status() {
        return STATUS;
    }

}