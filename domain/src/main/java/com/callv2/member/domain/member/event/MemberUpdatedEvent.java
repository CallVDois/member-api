package com.callv2.member.domain.member.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.callv2.member.domain.event.Event;
import com.callv2.member.domain.event.EventEntity;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.valueobject.System;

public class MemberUpdatedEvent extends Event<MemberUpdatedEvent.Data> {

    private static final String ENTITY = "member";
    private static final String ACTION = "updated";
    private static final String VERSION = "1.0.0";

    private MemberUpdatedEvent() {
        super(ENTITY, ACTION, VERSION, null, null, null);
    }

    private MemberUpdatedEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            MemberUpdatedEvent.Data data) {
        super(ENTITY, ACTION, VERSION, occurredAt, relatedEntities, data);
    }

    public record Data(
            UUID id,
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

    public static MemberUpdatedEvent create(final Member member) {
        return new MemberUpdatedEvent(Instant.now(), Set.of(EventEntity.of(member)), Data.of(member));
    }

    public static String eventKey() {
        return new MemberUpdatedEvent().key();
    }

}
