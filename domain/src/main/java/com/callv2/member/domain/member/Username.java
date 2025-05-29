package com.callv2.member.domain.member;

import com.callv2.member.domain.ValueObject;
import com.callv2.member.domain.validation.Error;
import com.callv2.member.domain.validation.ValidationHandler;

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
