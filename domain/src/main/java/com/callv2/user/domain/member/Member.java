package com.callv2.user.domain.member;

import java.time.Instant;

import com.callv2.user.domain.AggregateRoot;
import com.callv2.user.domain.validation.ValidationHandler;

public class Member extends AggregateRoot<MemberID> {

    private Username username;
    private Email email;
    private Nickname nickname;

    private boolean active;

    private Instant createdAt;
    private Instant updatedAt;

    private Member(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt) {
        super(id);
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Member create(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname) {

        final Instant now = Instant.now();

        return new Member(id, username, email, nickname, false, now, now);
    }

    public static Member with(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt) {
        return new Member(id, username, email, nickname, active, createdAt, updatedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new MemberValidator(this, handler).validate();
    }

    public Member activate() {

        if (this.active)
            return this;

        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Member deactivate() {

        if (!this.active)
            return this;

        this.active = false;
        this.updatedAt = Instant.now();
        return this;
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

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
