package com.callv2.member.infrastructure.member.presenter;

import com.callv2.member.application.member.retrieve.get.GetMemberOutput;
import com.callv2.member.application.member.retrieve.list.MemberListOutput;
import com.callv2.member.infrastructure.member.model.GetMemberResponse;
import com.callv2.member.infrastructure.member.model.MemberListResponse;

public interface MemberPresenter {

    static GetMemberResponse present(GetMemberOutput output) {
        return new GetMemberResponse(
                output.id(),
                output.username(),
                output.email(),
                output.nickname(),
                output.active(),
                output.availableSystems(),
                output.createdAt(),
                output.updatedAt());
    }

    static MemberListResponse present(MemberListOutput output) {
        return new MemberListResponse(
                output.id(),
                output.username(),
                output.email(),
                output.nickname(),
                output.active(),
                output.createdAt(),
                output.updatedAt());
    }

}
