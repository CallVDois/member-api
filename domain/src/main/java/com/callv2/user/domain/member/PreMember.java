package com.callv2.user.domain.member;

import com.callv2.user.domain.ValueObject;
import com.callv2.user.domain.validation.ValidationHandler;

public record PreMember(MemberUsername username, MemberEmail email) implements ValueObject {

    @Override
    public void validate(ValidationHandler aHandler) {
        this.username.validate(aHandler);
        this.email.validate(aHandler);
    }
}
