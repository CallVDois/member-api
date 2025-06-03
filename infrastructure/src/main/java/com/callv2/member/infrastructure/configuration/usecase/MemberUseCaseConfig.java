package com.callv2.member.infrastructure.configuration.usecase;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.member.application.member.activation.DefaultTogleMemberActivationUseCase;
import com.callv2.member.application.member.activation.TogleMemberActivationUseCase;
import com.callv2.member.application.member.create.CreateMemberUseCase;
import com.callv2.member.application.member.create.DefaultCreateMemberUseCase;
import com.callv2.member.application.member.retrieve.get.DefaultGetMemberUseCase;
import com.callv2.member.application.member.retrieve.get.GetMemberUseCase;
import com.callv2.member.application.member.retrieve.list.DefaultListMembersUseCase;
import com.callv2.member.application.member.retrieve.list.ListMembersUseCase;
import com.callv2.member.domain.event.EventDispatcher;
import com.callv2.member.domain.member.gateway.MemberGateway;

@Configuration
public class MemberUseCaseConfig {

    private final EventDispatcher eventDispatcher;
    private final MemberGateway memberGateway;

    public MemberUseCaseConfig(
            final MemberGateway memberGateway,
            final EventDispatcher eventDispatcher) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
    }

    @Bean
    CreateMemberUseCase createMemberUseCase() {
        return new DefaultCreateMemberUseCase(memberGateway, eventDispatcher);
    }

    @Bean
    TogleMemberActivationUseCase togleMemberActivationUseCase() {
        return new DefaultTogleMemberActivationUseCase(memberGateway);
    }

    @Bean
    GetMemberUseCase getMemberUseCase() {
        return new DefaultGetMemberUseCase(memberGateway);
    }

    @Bean
    ListMembersUseCase listMembersUseCase() {
        return new DefaultListMembersUseCase(memberGateway);
    }

}
