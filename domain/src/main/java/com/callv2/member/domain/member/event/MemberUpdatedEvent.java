package com.callv2.member.domain.member.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.callv2.member.domain.event.Event;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.valueobject.System;

public record MemberUpdatedEvent(
        String id,
        String source,
        MemberUpdatedEvent.Data data,
        Instant occurredAt) implements Event<MemberUpdatedEvent.Data> {

    private static final String NAME = "member.updated";

    @Override
    public String name() {
        return NAME;
    }

    public record Data(
            String id,
            String username,
            String email,
            String nickname,
            boolean isActive,
            Set<System> systems,
            Instant createdAt,
            Instant updatedAt,
            Long synchronizedVersion) implements Serializable {

        public static Data of(final Member member) {
            return new Data(
                    member.getId().getValue(),
                    member.getUsername().value(),
                    member.getEmail().value(),
                    member.getNickname().value(),
                    member.isActive(),
                    member.getAvailableSystems(),
                    member.getCreatedAt(),
                    member.getUpdatedAt(),
                    member.getSynchronizedVersion());
        }
    }

    public static MemberUpdatedEvent create(final String source, final MemberUpdatedEvent.Data data) {
        return new MemberUpdatedEvent(UUID.randomUUID().toString(), source, data, Instant.now());
    }

}
