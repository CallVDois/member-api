package com.callv2.member.application.member.create;

import java.util.Objects;

import com.callv2.member.domain.event.EventDispatcher;
import com.callv2.member.domain.exception.ValidationException;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.gateway.MemberGateway;
import com.callv2.member.domain.member.valueobject.Email;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.Password;
import com.callv2.member.domain.member.valueobject.PreMember;
import com.callv2.member.domain.member.valueobject.Username;
import com.callv2.member.domain.validation.handler.Notification;

public class DefaultCreateMemberUseCase extends CreateMemberUseCase {

    private final MemberGateway memberGateway;

    private final EventDispatcher eventDispatcher;

    public DefaultCreateMemberUseCase(
            final MemberGateway memberGateway,
            final EventDispatcher eventDispatcher) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
    }

    @Override
    public CreateMemberOutput execute(final CreateMemberInput input) {

        final Username username = Username.of(input.username());
        final Nickname nickname = Nickname.of(input.username());
        final Email email = Email.of(input.email());

        final Password password = Password.of(input.password());

        final PreMember preMember = PreMember.with(username, email, password);
        final Notification notification = Notification.create();

        nickname.validate(notification);
        preMember.validate(notification);

        if (notification.hasError())
            throw ValidationException.with("Validation error", notification.getErrors());

        final Member member = this.memberGateway.create(preMember);
        this.eventDispatcher.notify(member);

        return CreateMemberOutput.with(member.getId().getValue());
    }

}
