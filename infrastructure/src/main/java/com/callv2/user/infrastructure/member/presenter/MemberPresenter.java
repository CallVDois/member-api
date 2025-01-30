package com.callv2.user.infrastructure.member.presenter;

import com.callv2.user.application.member.retrieve.get.GetMemberOutput;
import com.callv2.user.infrastructure.member.model.GetMemberResponse;

public interface MemberPresenter {

    static GetMemberResponse present(GetMemberOutput output) {
        return new GetMemberResponse(
                output.id(),
                output.username(),
                output.email(),
                output.nickname(),
                output.active(),
                output.createdAt(),
                output.updatedAt());
    }

}
