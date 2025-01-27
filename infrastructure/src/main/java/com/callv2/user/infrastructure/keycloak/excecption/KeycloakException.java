package com.callv2.user.infrastructure.keycloak.excecption;

import org.springframework.http.HttpStatus;

import com.callv2.user.domain.exception.NoStacktraceException;

public abstract class KeycloakException extends NoStacktraceException {

    protected KeycloakException(final String message) {
        super(message);
    }

    protected KeycloakException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public abstract HttpStatus status();

}
