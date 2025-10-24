package com.callv2.member.domain.member.valueobject;

import com.callv2.member.domain.ValueObject;
import com.callv2.member.domain.validation.Error;
import com.callv2.member.domain.validation.ValidationHandler;

public record Nickname(String value) implements ValueObject {

    private static final String NICKNAME_PATTERN = "^(?![._])(?!.*[._]{2})[A-Za-z0-9._]{3,20}$";

    public static Nickname of(final String nickname) {
        return new Nickname(nickname);
    }

    @Override
    public void validate(ValidationHandler aHandler) {
        if (value == null || value.isBlank()) {
            aHandler.append(Error.with("'nickname' is required"));
            return;
        }
        if (!value.matches(NICKNAME_PATTERN)) 
            aHandler.append(Error.with("'nickname' must be 3 to 20 characters long, only letters, digits, underscores, dots, and cannot start/end with or have consecutive special characters."));

    }

}
