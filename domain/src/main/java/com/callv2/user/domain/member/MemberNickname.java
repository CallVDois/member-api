package com.callv2.user.domain.member;

import com.callv2.user.domain.ValueObject;
import com.callv2.user.domain.validation.Error;
import com.callv2.user.domain.validation.ValidationHandler;

public record MemberNickname(String value) implements ValueObject {

    @Override
    public void validate(ValidationHandler aHandler) {
        if (value == null || value.isBlank())
            aHandler.append(Error.with("'nickname' is required"));
    }

}
