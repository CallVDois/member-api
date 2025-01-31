package com.callv2.user.application.member.retrieve.list;

import java.time.Instant;

import com.callv2.user.domain.member.Member;

public record MemberListOutput(
        String id,
        String username,
        String email,
        String nickname,
        boolean active,
        Instant createdAt,
        Instant updatedAt) {

    public static MemberListOutput from(Member member) {
        return new MemberListOutput(
                member.getId().getValue(),
                member.getUsername().value(),
                member.getEmail().value(),
                member.getNickname().value(),
                member.isActive(),
                member.getCreatedAt(),
                member.getUpdatedAt());
    }

}
