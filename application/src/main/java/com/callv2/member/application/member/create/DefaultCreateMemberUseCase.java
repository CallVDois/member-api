package com.callv2.member.application.member.create;

import java.util.Objects;

import com.callv2.member.domain.exception.DomainException;
import com.callv2.member.domain.member.Email;
import com.callv2.member.domain.member.MemberGateway;
import com.callv2.member.domain.member.Nickname;
import com.callv2.member.domain.member.Password;
import com.callv2.member.domain.member.PreMember;
import com.callv2.member.domain.member.Username;
import com.callv2.member.domain.validation.handler.Notification;

public class DefaultCreateMemberUseCase extends CreateMemberUseCase {

    private final MemberGateway memberGateway;

    public DefaultCreateMemberUseCase(final MemberGateway memberGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
    }

    @Override
    public CreateMemberOutput execute(final CreateMemberInput anIn) {

        final Username username = Username.of(anIn.username());
        final Nickname nickname = Nickname.of(anIn.username());
        final Email email = Email.of(anIn.email());

        final Password password = Password.of(anIn.password());

        final PreMember preMember = PreMember.with(username, email, password);
        final Notification notification = Notification.create();

        nickname.validate(notification);
        preMember.validate(notification);

        if (notification.hasError())
            throw DomainException.with(notification.getErrors());

        return CreateMemberOutput.with(this.memberGateway.create(preMember).getId().getValue());
    }

}
