package com.callv2.member.infrastructure.member.model;

import java.time.Instant;
import java.util.Set;

import com.callv2.member.domain.member.valueobject.System;

public record GetMemberResponse(
        String id,
        String username,
        String email,
        String nickname,
        boolean active,
        Set<System> availableSystems,
        Instant createdAt,
        Instant updatedAt) {

}
