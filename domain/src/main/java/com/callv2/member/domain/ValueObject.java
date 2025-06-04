package com.callv2.member.domain;

import com.callv2.member.domain.validation.ValidationHandler;

public interface ValueObject extends Validatable {

    @Override
    default void validate(ValidationHandler aHandler) {
    };

}
