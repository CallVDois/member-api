package com.callv2.member.domain.member.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import com.callv2.member.domain.AggregateRoot;
import com.callv2.member.domain.event.Event;
import com.callv2.member.domain.event.EventSource;
import com.callv2.member.domain.member.event.MemberCreatedEvent;
import com.callv2.member.domain.member.event.MemberUpdatedEvent;
import com.callv2.member.domain.member.validation.MemberValidator;
import com.callv2.member.domain.member.valueobject.Email;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.System;
import com.callv2.member.domain.member.valueobject.Username;
import com.callv2.member.domain.validation.ValidationHandler;

public class Member extends AggregateRoot<MemberID> implements EventSource {

    private Queue<Event<?>> events;

    private Username username;
    private Email email;
    private Nickname nickname;

    private Set<System> availableSystems;

    private boolean active;

    private Instant createdAt;
    private Instant updatedAt;

    private Member(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname,
            final boolean active,
            final Set<System> availableSystems,
            final Instant createdAt,
            final Instant updatedAt) {
        super(id);
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        this.availableSystems = availableSystems != null ? new HashSet<>(availableSystems) : new HashSet<>();
        this.events = new LinkedList<>();
    }

    public static Member create(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname) {

        final Instant now = Instant.now();

        final Member member = new Member(id, username, email, nickname, false, new HashSet<>(), now, now);
        member.events.add(MemberCreatedEvent.create("MemberAggregate", MemberCreatedEvent.Data.of(member)));
        return member;
    }

    public static Member with(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname,
            final boolean active,
            final Set<System> availableSystems,
            final Instant createdAt,
            final Instant updatedAt) {
        return new Member(id, username, email, nickname, active, availableSystems, createdAt, updatedAt);
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
        this.events.add(MemberUpdatedEvent.create("MemberAggregate", MemberUpdatedEvent.Data.of(this)));
        return this;
    }

    public Member deactivate() {

        if (!this.active)
            return this;

        this.active = false;
        this.updatedAt = Instant.now();
        this.events.add(MemberUpdatedEvent.create("MemberAggregate", MemberUpdatedEvent.Data.of(this)));
        return this;
    }

    public Member addSystem(final System system) {
        if (this.availableSystems.add(system)) {
            this.updatedAt = Instant.now();
            this.events.add(MemberUpdatedEvent.create("MemberAggregate", MemberUpdatedEvent.Data.of(this)));
        }

        return this;
    }

    public Member removeSystem(final System system) {
        if (this.availableSystems.remove(system)) {
            this.updatedAt = Instant.now();
            this.events.add(MemberUpdatedEvent.create("MemberAggregate", MemberUpdatedEvent.Data.of(this)));
        }
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

    public Set<System> getAvailableSystems() {
        return Set.copyOf(availableSystems);
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
