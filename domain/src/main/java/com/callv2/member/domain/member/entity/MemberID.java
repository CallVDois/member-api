package com.callv2.member.domain.member.entity;

import java.util.UUID;

import com.callv2.member.domain.Identifier;

public class MemberID extends Identifier<UUID> {

    public MemberID(UUID value) {
        super(value);
    }

    public static MemberID of(final UUID id) {
        return new MemberID(id);
    }

    public static MemberID of(final String id) {
        return new MemberID(UUID.fromString(id));
    }

    @Override
    public String getStringValue() {
        return this.id.toString();
    }

}
