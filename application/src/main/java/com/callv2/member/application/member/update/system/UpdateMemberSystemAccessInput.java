package com.callv2.member.application.member.update.system;

import java.util.Set;
import java.util.UUID;

import com.callv2.member.domain.member.valueobject.System;

public record UpdateMemberSystemAccessInput(UUID memberId, Set<System> systems) {

    public static UpdateMemberSystemAccessInput of(final UUID memberId, final Set<System> systems) {
        return new UpdateMemberSystemAccessInput(memberId, systems);
    }

}
