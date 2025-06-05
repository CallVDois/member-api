package com.callv2.member.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException {

    private NotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static NotFoundException from(final String message) {
        return new NotFoundException(message);
    }

}