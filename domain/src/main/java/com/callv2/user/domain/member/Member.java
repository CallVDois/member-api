package com.callv2.user.domain.member;

import com.callv2.user.domain.AggregateRoot;
import com.callv2.user.domain.validation.ValidationHandler;

public class Member extends AggregateRoot<MemberID> {

    private Username username;
    private Email email;
    private Nickname nickname;

    private Member(final MemberID anID) {
        super(anID);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new MemberValidator(this, handler).validate();
    }

    public Username getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    public Nickname getNickname() {
        return nickname;
    }

}
