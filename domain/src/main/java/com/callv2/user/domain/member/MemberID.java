package com.callv2.user.domain.member;

import com.callv2.user.domain.Identifier;

public class MemberID extends Identifier<String> {

    public MemberID(String value) {
        super(value);
    }

    public static MemberID of(final String id) {
        return new MemberID(id);
    }

}
