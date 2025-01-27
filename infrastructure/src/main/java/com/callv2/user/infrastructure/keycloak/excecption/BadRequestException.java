package com.callv2.user.infrastructure.keycloak.excecption;

import java.util.function.Predicate;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.callv2.user.infrastructure.keycloak.model.ErrorRepresentation;

public class BadRequestException extends KeycloakException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private final ErrorRepresentation error;

    private BadRequestException(final ErrorRepresentation error) {
        super(error.errorMessage());
        this.error = error;
    }

    public static BadRequestException from(final ErrorRepresentation error) {
        return new BadRequestException(error);
    }

    @Override
    public HttpStatus status() {
        return STATUS;
    }

    public static Predicate<HttpStatusCode> isSame() {
        return STATUS::isSameCodeAs;
    }

    // public static Function<ClientResponse, Mono<? extends Throwable>>
    // monoException() {
    // return res -> res
    // .bodyToMono(ErrorRepresentation.class)
    // .flatMap(error -> Mono.error(from(error)));
    // }

    public ErrorRepresentation getError() {
        return error;
    }

}
