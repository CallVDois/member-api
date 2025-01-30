package com.callv2.user.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException {

    private ForbiddenException(final String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

    public static ForbiddenException from(final String message) {
        return new ForbiddenException(message);
    }

}