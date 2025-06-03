package com.callv2.member.domain.member.entity;

import com.callv2.member.domain.Identifier;

public class MemberID extends Identifier<String> {

    public MemberID(String value) {
        super(value);
    }

    public static MemberID of(final String id) {
        return new MemberID(id);
    }

}
