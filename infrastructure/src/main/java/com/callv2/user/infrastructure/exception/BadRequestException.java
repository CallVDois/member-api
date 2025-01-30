package com.callv2.user.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpException {

    private BadRequestException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public static BadRequestException from(final String message) {
        return new BadRequestException(message);
    }

}
