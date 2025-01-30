package com.callv2.user.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.user.application.member.activation.DefaultTogleMemberActivationUseCase;
import com.callv2.user.application.member.activation.TogleMemberActivationUseCase;
import com.callv2.user.application.member.create.CreateMemberUseCase;
import com.callv2.user.application.member.create.DefaultCreateMemberUseCase;
import com.callv2.user.application.member.quota.request.approve.ApproveRequestQuotaUseCase;
import com.callv2.user.application.member.quota.request.approve.DefaultApproveRequestQuotaUseCase;
import com.callv2.user.application.member.quota.request.create.CreateRequestQuotaUseCase;
import com.callv2.user.application.member.quota.request.create.DefaultCreateRequestQuotaUseCase;
import com.callv2.user.application.member.quota.request.list.DefaultListRequestQuotaUseCase;
import com.callv2.user.application.member.quota.request.list.ListRequestQuotaUseCase;
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

    @Bean
    CreateRequestQuotaUseCase createRequestQuotaUseCase() {
        return new DefaultCreateRequestQuotaUseCase(memberGateway);
    }

    @Bean
    ApproveRequestQuotaUseCase approveRequestQuotaUseCase() {
        return new DefaultApproveRequestQuotaUseCase(memberGateway);
    }

    @Bean
    ListRequestQuotaUseCase listRequestQuotaUseCase() {
        return new DefaultListRequestQuotaUseCase(memberGateway);
    }

}
