package com.callv2.member.domain.event;

import com.callv2.member.domain.Entity;
import com.callv2.member.domain.Identifier;

public record EventEntity(String type, String id) {

    public static <E extends Entity<I>, I extends Identifier<?>> EventEntity of(final E entity) {
        return new EventEntity(entity.getClass().getSimpleName(), entity.getId().getStringValue());
    }

}
