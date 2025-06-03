package com.callv2.member.application.member.activation;

import java.util.Objects;

import com.callv2.member.domain.event.EventDispatcher;
import com.callv2.member.domain.exception.NotFoundException;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.gateway.MemberGateway;

public class DefaultTogleMemberActivationUseCase extends TogleMemberActivationUseCase {

    private final MemberGateway memberGateway;

    private final EventDispatcher eventDispatcher;

    public DefaultTogleMemberActivationUseCase(
            final MemberGateway memberGateway,
            final EventDispatcher eventDispatcher) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
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

        eventDispatcher.notify(memberGateway.update(member));
    }

}
