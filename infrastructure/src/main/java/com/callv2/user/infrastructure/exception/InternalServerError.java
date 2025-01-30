package com.callv2.user.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class InternalServerError extends HttpException {

    private InternalServerError(final String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static InternalServerError from(final String message) {
        return new InternalServerError(message);
    }

}
