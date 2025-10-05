package com.callv2.member.infrastructure.member.model;

import java.time.Instant;
import java.util.UUID;

public record MemberListResponse(
        UUID id,
        String username,
        String email,
        String nickname,
        boolean active,
        Instant createdAt,
        Instant updatedAt) {

}
