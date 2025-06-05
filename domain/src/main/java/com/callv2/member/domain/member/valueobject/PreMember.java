package com.callv2.member.domain.member.valueobject;

import com.callv2.member.domain.ValueObject;
import com.callv2.member.domain.validation.ValidationHandler;

public record PreMember(Username username, Email email, Password password) implements ValueObject {

    public static PreMember with(
            final Username username,
            final Email email,
            final Password password) {
        return new PreMember(username, email, password);
    }

    @Override
    public void validate(ValidationHandler aHandler) {
        this.username.validate(aHandler);
        this.email.validate(aHandler);
    }
}
