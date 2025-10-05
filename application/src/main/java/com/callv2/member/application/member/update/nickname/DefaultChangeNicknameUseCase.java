package com.callv2.member.application.member.update.nickname;

import java.util.Objects;

import com.callv2.member.domain.event.EventDispatcher;
import com.callv2.member.domain.exception.NotFoundException;
import com.callv2.member.domain.exception.ValidationException;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.gateway.MemberGateway;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.validation.handler.Notification;

public class DefaultChangeNicknameUseCase extends ChangeNicknameUseCase {

    private final MemberGateway memberGateway;
    private final EventDispatcher eventDispatcher;

    public DefaultChangeNicknameUseCase(
            final MemberGateway memberGateway,
            final EventDispatcher eventDispatcher) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
    }

    @Override
    public ChangeNicknameOutput execute(final ChangeNicknameInput input) {

        final MemberID memberId = MemberID.of(input.memberId());
        final Nickname nickname = Nickname.of(input.nickname());

        final Notification notification = Notification.create();

        nickname.validate(notification);

        if (notification.hasError())
            throw ValidationException.with("Validation error", notification.getErrors());
        
        final Member member = this.memberGateway.findById(memberId)
                .orElseThrow(() -> NotFoundException.with(Member.class, memberId.getStringValue()));

        member.changeNickname(nickname);
        
        this.eventDispatcher.notify(member);

        return ChangeNicknameOutput.with(member.getId().getValue());
    }
}
