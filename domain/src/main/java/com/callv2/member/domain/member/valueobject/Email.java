package com.callv2.member.domain.member.valueobject;

import com.callv2.member.domain.ValueObject;
import com.callv2.member.domain.validation.ValidationHandler;

public record Email(String value) implements ValueObject {

    public static Email of(final String email) {
        return new Email(email);
    }

    @Override
    public void validate(ValidationHandler aHandler) {

    }

}
