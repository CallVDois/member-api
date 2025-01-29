package com.callv2.user.application.member.activation;

import java.util.Objects;

import com.callv2.user.domain.exception.NotFoundException;
import com.callv2.user.domain.member.Member;
import com.callv2.user.domain.member.MemberGateway;
import com.callv2.user.domain.member.MemberID;

public class DefaultTogleMemberActivationUseCase extends TogleMemberActivationUseCase {

    private final MemberGateway memberGateway;

    public DefaultTogleMemberActivationUseCase(final MemberGateway memberGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
    }

    @Override
    public void execute(TogleMemberActivationInput input) {

        final Member member = memberGateway
                .findById(MemberID.of(input.memberId()))
                .orElseThrow(() -> NotFoundException.with(Member.class, input.memberId()));

        if (input.active())
            member.activate();
        else
            member.deactivate();

        memberGateway.update(member);
    }

}
