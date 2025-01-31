package com.callv2.user.infrastructure.member.model;

import java.time.Instant;

public record MemberListResponse(
        String id,
        String username,
        String email,
        String nickname,
        boolean active,
        Instant createdAt,
        Instant updatedAt) {

}
