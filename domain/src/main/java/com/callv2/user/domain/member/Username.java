package com.callv2.user.domain.member;

import com.callv2.user.domain.ValueObject;
import com.callv2.user.domain.validation.Error;
import com.callv2.user.domain.validation.ValidationHandler;

public record Username(String value) implements ValueObject {

    public static Username of(final String username) {
        return new Username(username);
    }

    @Override
    public void validate(ValidationHandler aHandler) {
        if (value == null || value.isBlank())
            aHandler.append(Error.with("'username' is required"));

        if (value.contains(" "))
            aHandler.append(Error.with("'username' cannot contain spaces"));
    }

}
