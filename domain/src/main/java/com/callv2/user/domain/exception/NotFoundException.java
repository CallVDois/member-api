package com.callv2.user.domain.exception;

import java.util.List;

import com.callv2.user.domain.Entity;
import com.callv2.user.domain.Identifier;
import com.callv2.user.domain.validation.Error;

public class NotFoundException extends DomainException {

    private NotFoundException(final String message) {
        super(message, List.of(Error.with(message)));
    }

    public static NotFoundException with(
            final Class<? extends Entity<? extends Identifier<?>>> entityClass) {

        final String message = String.format(
                "%s not found",
                entityClass.getSimpleName());

        return new NotFoundException(message);
    }

    public static NotFoundException with(
            final Class<? extends Entity<? extends Identifier<?>>> entityClass,
            final String id) {

        final String message = String.format(
                "%s with id '%s' not found",
                entityClass.getSimpleName(),
                id);

        return new NotFoundException(message);
    }

}
