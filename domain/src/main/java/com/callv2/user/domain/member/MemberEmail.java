package com.callv2.user.domain.member;

import com.callv2.user.domain.ValueObject;
import com.callv2.user.domain.validation.ValidationHandler;

public record MemberEmail(String value) implements ValueObject {

    @Override
    public void validate(ValidationHandler aHandler) {

    }

}
