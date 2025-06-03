package com.callv2.member.domain.member;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.callv2.member.domain.event.Event;

public record MemberCreatedEvent(
        String id,
        String source,
        MemberCreatedEvent.Data data,
        Instant occurredAt) implements Event<MemberCreatedEvent.Data> {

    private static final String NAME = "member.created";

    @Override
    public String name() {
        return NAME;
    }

    public record Data(
            String username,
            String email,
            String nickname,
            boolean isActive,
            Instant createdAt,
            Instant updatedAt) implements Serializable {

        public static Data of(final Member member) {
            return new Data(
                    member.getUsername().value(),
                    member.getEmail().value(),
                    member.getNickname().value(),
                    member.isActive(),
                    member.getCreatedAt(),
                    member.getUpdatedAt());
        }
    }

    public static MemberCreatedEvent create(final String source, final MemberCreatedEvent.Data data) {
        return new MemberCreatedEvent(UUID.randomUUID().toString(), source, data, Instant.now());
    }

}
