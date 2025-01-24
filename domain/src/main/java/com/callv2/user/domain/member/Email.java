package com.callv2.user.domain.member;

import com.callv2.user.domain.ValueObject;
import com.callv2.user.domain.validation.ValidationHandler;

public record Email(String value) implements ValueObject {

    public static Email of(final String email) {
        return new Email(email);
    }

    @Override
    public void validate(ValidationHandler aHandler) {

    }

}
