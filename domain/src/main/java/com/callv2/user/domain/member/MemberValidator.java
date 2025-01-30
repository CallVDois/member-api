package com.callv2.user.domain.member;

import com.callv2.user.domain.validation.Error;
import com.callv2.user.domain.validation.ValidationHandler;
import com.callv2.user.domain.validation.Validator;

public class MemberValidator extends Validator {

    private final Member member;

    protected MemberValidator(final Member aMember, final ValidationHandler aHandler) {
        super(aHandler);
        this.member = aMember;
    }

    @Override
    public void validate() {
        validateId();
        validateUsername();
        validateNickname();
    }

    private void validateId() {
        if (this.member.getId() == null) {
            this.validationHandler().append(Error.with("'id' is required"));
            return;
        }

        this.member.getId().validate(this.validationHandler());
    }

    private void validateUsername() {
        if (this.member.getUsername() == null) {
            this.validationHandler().append(Error.with("'username' is required"));
            return;
        }

        this.member.getUsername().validate(this.validationHandler());
    }

    private void validateNickname() {
        if (this.member.getNickname() == null) {
            this.validationHandler().append(Error.with("'nickname' is required"));
            return;
        }

        this.member.getNickname().validate(this.validationHandler());
    }

}
