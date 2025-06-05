package com.callv2.member.application.member.update.system;

import java.util.Set;

import com.callv2.member.domain.member.valueobject.System;

public record UpdateMemberSystemAccessInput(String memberId, Set<System> systems) {

    public static UpdateMemberSystemAccessInput of(final String memberId, final Set<System> systems) {
        return new UpdateMemberSystemAccessInput(memberId, systems);
    }

}
