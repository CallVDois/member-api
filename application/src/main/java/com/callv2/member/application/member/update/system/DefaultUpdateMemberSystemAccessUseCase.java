package com.callv2.member.application.member.update.system;

import java.util.Objects;

import com.callv2.member.domain.event.EventDispatcher;
import com.callv2.member.domain.exception.NotFoundException;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.gateway.MemberGateway;

public class DefaultUpdateMemberSystemAccessUseCase extends UpdateMemberSystemAccessUseCase {

    private final MemberGateway memberGateway;

    private final EventDispatcher eventDispatcher;

    public DefaultUpdateMemberSystemAccessUseCase(
            final MemberGateway memberGateway,
            final EventDispatcher eventDispatcher) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
    }

    @Override
    public void execute(final UpdateMemberSystemAccessInput input) {

        final Member member = memberGateway
                .findById(MemberID.of(input.memberId()))
                .orElseThrow(() -> NotFoundException.with(Member.class, input.memberId()));

        member
                .getAvailableSystems()
                .stream()
                .filter(system -> !input.systems().contains(system))
                .forEach(member::removeSystem);

        input.systems().forEach(member::addSystem);

        eventDispatcher.notify(memberGateway.update(member));
    }

}
