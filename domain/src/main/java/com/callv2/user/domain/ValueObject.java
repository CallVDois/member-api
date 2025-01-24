package com.callv2.user.domain;

import com.callv2.user.domain.validation.ValidationHandler;

public interface ValueObject {

    default void validate(ValidationHandler aHandler) {
    };

}
