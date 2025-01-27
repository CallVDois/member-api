package com.callv2.user.infrastructure.keycloak.exception;

import java.util.function.Predicate;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.callv2.user.infrastructure.keycloak.model.Error;

public class UnauthorizedException extends KeycloakException {

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    private UnauthorizedException(final Error error) {
        super(error.error());
    }

    public static UnauthorizedException from(final Error error) {
        return new UnauthorizedException(error);
    }

    public static Predicate<HttpStatusCode> isSame() {
        return STATUS::isSameCodeAs;
    }

    public HttpStatus status() {
        return STATUS;
    }

}