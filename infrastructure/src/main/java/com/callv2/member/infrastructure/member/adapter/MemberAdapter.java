package com.callv2.member.infrastructure.member.adapter;

import java.util.UUID;

import com.callv2.member.application.member.create.CreateMemberInput;
import com.callv2.member.application.member.update.nickname.UpdateNicknameInput;
import com.callv2.member.infrastructure.member.model.UpdateNicknameRequest;
import com.callv2.member.infrastructure.member.model.CreateMemberRequest;

public interface MemberAdapter {

    static CreateMemberInput adapt(CreateMemberRequest request) {
        return CreateMemberInput.with(request.username(), request.email(), request.password());
    }

    static UpdateNicknameInput adapt(UUID memberId, UpdateNicknameRequest request) {
        return UpdateNicknameInput.with(memberId, request.nickname());
    }
}
