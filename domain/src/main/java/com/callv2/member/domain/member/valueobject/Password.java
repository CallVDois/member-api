package com.callv2.member.domain.member.valueobject;

import com.callv2.member.domain.ValueObject;
import com.callv2.member.domain.validation.Error;
import com.callv2.member.domain.validation.ValidationHandler;

public record Password(String value) implements ValueObject {

    public static Password of(final String password) {
        return new Password(password);
    }

    @Override
    public void validate(ValidationHandler aHandler) {
        if (this.value == null || this.value.isBlank())
            aHandler.append(Error.with("Password is required."));
    }

    @Override
    public final String toString() {
        return "Password{value=****}";
    }

}
