package com.callv2.user.infrastructure.member.adapter;

import com.callv2.user.application.member.create.CreateMemberInput;
import com.callv2.user.infrastructure.member.model.CreateMemberRequest;

public interface MemberAdapter {

    static CreateMemberInput adapt(CreateMemberRequest request) {
        return CreateMemberInput.with(request.username(), request.email(), request.password());
    }

}
