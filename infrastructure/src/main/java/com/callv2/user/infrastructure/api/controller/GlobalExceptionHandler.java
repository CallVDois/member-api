package com.callv2.user.infrastructure.api.controller;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.callv2.user.domain.exception.DomainException;
import com.callv2.user.domain.exception.InternalErrorException;
import com.callv2.user.domain.exception.NotFoundException;
import com.callv2.user.domain.exception.ValidationException;
import com.callv2.user.infrastructure.exception.HttpException;
import com.callv2.user.infrastructure.exception.InternalServerError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<ApiError> handle(final Throwable ex) {
        return ResponseEntity.internalServerError().body(ApiError.with("Internal Server Error" + ex.getMessage()));
    }

    @ExceptionHandler(value = InternalErrorException.class)
    public ResponseEntity<ApiError> handle(final InternalErrorException ex) {
        return ResponseEntity.unprocessableEntity().body(ApiError.with("Internal Server Error"));
    }

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<ApiError> handle(final DomainException ex) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex));
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ApiError> handle(final ValidationException ex) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Void> handle(final NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiError> handle(final InvalidDataAccessApiUsageException ex) {
        return ResponseEntity.badRequest()
                .body(ApiError.with("Invalid Data Access Api Usage [%s]".formatted(ex.getMessage())));
    }

    @ExceptionHandler(value = InternalServerError.class)
    public ResponseEntity<ApiError> handle(final InternalServerError ex) {
        return ResponseEntity.status(ex.getStatus()).body(ApiError.with("Internal Server Error"));
    }

    @ExceptionHandler(value = HttpException.class)
    public ResponseEntity<ApiError> handle(final HttpException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ApiError.with(ex.getMessage()));
    }

}
