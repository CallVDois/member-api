package com.callv2.member.domain;

import com.callv2.member.domain.validation.ValidationHandler;

public interface ValueObject {

    default void validate(ValidationHandler aHandler) {
    };

}
