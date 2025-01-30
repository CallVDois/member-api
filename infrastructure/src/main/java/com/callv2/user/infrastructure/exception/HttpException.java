package com.callv2.user.infrastructure.exception;

import org.springframework.http.HttpStatus;

import com.callv2.user.domain.exception.NoStacktraceException;

public abstract class HttpException extends NoStacktraceException {

    private final HttpStatus status;

    protected HttpException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
