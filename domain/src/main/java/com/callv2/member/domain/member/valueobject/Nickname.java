package com.callv2.member.domain.member.valueobject;

import com.callv2.member.domain.ValueObject;
import com.callv2.member.domain.validation.Error;
import com.callv2.member.domain.validation.ValidationHandler;

public record Nickname(String value) implements ValueObject {

    public static Nickname of(final String nickname) {
        return new Nickname(nickname);
    }

    @Override
    public void validate(ValidationHandler aHandler) {
        if (value == null || value.isBlank())
            aHandler.append(Error.with("'nickname' is required"));
    }

}
