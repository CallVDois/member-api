package com.callv2.member.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpException {

    private UnauthorizedException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public static UnauthorizedException from(final String message) {
        return new UnauthorizedException(message);
    }

}