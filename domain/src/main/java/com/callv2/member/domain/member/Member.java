package com.callv2.member.domain.member;

import java.time.Instant;
import java.util.Optional;
import java.util.Queue;

import com.callv2.member.domain.AggregateRoot;
import com.callv2.member.domain.event.Event;
import com.callv2.member.domain.event.EventSource;
import com.callv2.member.domain.validation.ValidationHandler;

public class Member extends AggregateRoot<MemberID> implements EventSource {

    private Queue<Event<?>> events;

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

        this.events = new java.util.LinkedList<>();
    }

    public static Member create(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname) {

        final Instant now = Instant.now();

        final Member member = new Member(id, username, email, nickname, false, now, now);
        member.events
                .add(MemberCreatedEvent.create(
                        "MemberAggregate",
                        MemberCreatedEvent.Data.of(member)));
        return member;
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

    @Override
    public Optional<Event<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
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
