package com.callv2.member.infrastructure.api.controller;

import java.util.List;

import com.callv2.member.domain.exception.DomainException;
import com.callv2.member.domain.validation.Error;

public record ApiError(String message, List<Error> errors) {

    static ApiError with(final String message) {
        return new ApiError(message, List.of());
    }

    static ApiError from(final DomainException ex) {
        return new ApiError(ex.getMessage(), ex.getErrors());
    }
}