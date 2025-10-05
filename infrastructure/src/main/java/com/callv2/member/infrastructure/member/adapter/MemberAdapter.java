package com.callv2.member.infrastructure.member.adapter;

import java.util.UUID;

import com.callv2.member.application.member.create.CreateMemberInput;
import com.callv2.member.application.member.update.nickname.ChangeNicknameInput;
import com.callv2.member.infrastructure.member.model.ChangeNicknameRequest;
import com.callv2.member.infrastructure.member.model.CreateMemberRequest;

public interface MemberAdapter {

    static CreateMemberInput adapt(CreateMemberRequest request) {
        return CreateMemberInput.with(request.username(), request.email(), request.password());
    }

    static ChangeNicknameInput adapt(UUID memberId, ChangeNicknameRequest request) {
        return ChangeNicknameInput.with(memberId, request.nickname());
    }
}
