package com.callv2.member.domain.exception;

import java.util.List;

import com.callv2.member.domain.validation.Error;
import com.callv2.member.domain.validation.handler.Notification;

public class ValidationException extends DomainException {

    private ValidationException(final String aMessage, final List<Error> anErrors) {
        super(aMessage, List.copyOf(anErrors));
    }

    public static ValidationException with(final String aMessage, final Notification aNotification) {
        return new ValidationException(aMessage, List.copyOf(aNotification.getErrors()));
    }

    public static ValidationException with(final String message, final Error error) {
        return new ValidationException(message, List.of(error));
    }

    public static ValidationException with(final String message, final List<Error> errors) {
        return new ValidationException(message, errors);
    }

}
