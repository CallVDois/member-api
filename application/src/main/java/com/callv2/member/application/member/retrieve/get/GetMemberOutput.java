package com.callv2.member.application.member.retrieve.get;

import java.time.Instant;

import com.callv2.member.domain.member.entity.Member;

public record GetMemberOutput(
        String id,
        String username,
        String email,
        String nickname,
        boolean active,
        Instant createdAt,
        Instant updatedAt) {

    public static GetMemberOutput from(Member member) {
        return new GetMemberOutput(
                member.getId().getValue(),
                member.getUsername().value(),
                member.getEmail().value(),
                member.getNickname().value(),
                member.isActive(),
                member.getCreatedAt(),
                member.getUpdatedAt());
    }

}
