package com.callv2.user.domain.member;

import com.callv2.user.domain.AggregateRoot;
import com.callv2.user.domain.validation.ValidationHandler;

public class Member extends AggregateRoot<MemberID> {

    private MemberUsername username;
    private MemberEmail email;
    private MemberNickname nickname;

    private Member(final MemberID anID) {
        super(anID);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new MemberValidator(this, handler).validate();
    }

    public MemberUsername getUsername() {
        return username;
    }

    public MemberEmail getEmail() {
        return email;
    }

    public MemberNickname getNickname() {
        return nickname;
    }

}
