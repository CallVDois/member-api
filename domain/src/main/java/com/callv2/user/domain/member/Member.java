package com.callv2.user.domain.member;

import java.time.Instant;

import com.callv2.user.domain.AggregateRoot;
import com.callv2.user.domain.validation.ValidationHandler;

public class Member extends AggregateRoot<MemberID> {

    private Username username;
    private Email email;
    private Nickname nickname;

    private Instant createdAt;
    private Instant updatedAt;

    private Member(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname,
            final Instant createdAt,
            final Instant updatedAt) {
        super(id);
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Member with(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname,
            final Instant createdAt,
            final Instant updatedAt) {
        return new Member(id, username, email, nickname, createdAt, updatedAt);
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
