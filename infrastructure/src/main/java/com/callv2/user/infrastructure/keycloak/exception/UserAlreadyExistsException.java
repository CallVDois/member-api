package com.callv2.user.infrastructure.keycloak.exception;

import java.util.function.Predicate;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.callv2.user.infrastructure.keycloak.model.ErrorRepresentation;

public class UserAlreadyExistsException extends KeycloakException {

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    private final ErrorRepresentation error;

    private UserAlreadyExistsException(final ErrorRepresentation error) {
        super(error.errorMessage());
        this.error = error;
    }

    public static UserAlreadyExistsException from(final ErrorRepresentation error) {
        return new UserAlreadyExistsException(error);
    }

    public static Predicate<HttpStatusCode> isSame() {
        return STATUS::isSameCodeAs;
    }

    public HttpStatus status() {
        return STATUS;
    }

    public ErrorRepresentation getError() {
        return error;
    }

}
