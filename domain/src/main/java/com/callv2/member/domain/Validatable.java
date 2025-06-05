package com.callv2.member.domain;

import com.callv2.member.domain.validation.ValidationHandler;

@FunctionalInterface
public interface Validatable {

    void validate(ValidationHandler aHandler);

}
