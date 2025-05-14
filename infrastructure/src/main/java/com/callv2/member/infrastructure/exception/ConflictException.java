package com.callv2.member.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends HttpException {

    private ConflictException(final String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public static ConflictException from(final String message) {
        return new ConflictException(message);
    }

}