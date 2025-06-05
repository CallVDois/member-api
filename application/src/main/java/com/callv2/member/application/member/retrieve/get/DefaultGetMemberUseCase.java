package com.callv2.member.application.member.retrieve.get;

import java.util.Objects;

import com.callv2.member.domain.exception.NotFoundException;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.gateway.MemberGateway;

public class DefaultGetMemberUseCase extends GetMemberUseCase {

    private final MemberGateway memberGateway;

    public DefaultGetMemberUseCase(final MemberGateway memberGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
    }

    @Override
    public GetMemberOutput execute(GetMemberInput input) {
        return memberGateway
                .findById(MemberID.of(input.id()))
                .map(GetMemberOutput::from)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.id()));
    }

}
