package com.callv2.member.infrastructure.member.model;

import java.util.UUID;

public record ChangeNicknameRequest(UUID memberId, String nickname) {

    @Override
    public String toString() {
        return "CreateMemberRequest [memberId=" + memberId + ", email=" + nickname + "]";
    }

}
