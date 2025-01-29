package com.callv2.user.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.user.application.member.activation.DefaultTogleMemberActivationUseCase;
import com.callv2.user.application.member.activation.TogleMemberActivationUseCase;
import com.callv2.user.application.member.create.CreateMemberUseCase;
import com.callv2.user.application.member.create.DefaultCreateMemberUseCase;
import com.callv2.user.domain.member.MemberGateway;

@Configuration
public class MemberUseCaseConfig {

    private final MemberGateway memberGateway;

    public MemberUseCaseConfig(final MemberGateway memberGateway) {
        this.memberGateway = memberGateway;
    }

    @Bean
    CreateMemberUseCase createMemberUseCase() {
        return new DefaultCreateMemberUseCase(memberGateway);
    }

    @Bean
    TogleMemberActivationUseCase togleMemberActivationUseCase() {
        return new DefaultTogleMemberActivationUseCase(memberGateway);
    }

}
