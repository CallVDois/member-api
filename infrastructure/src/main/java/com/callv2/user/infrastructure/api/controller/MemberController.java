package com.callv2.user.infrastructure.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.callv2.user.application.member.activation.TogleMemberActivationInput;
import com.callv2.user.application.member.activation.TogleMemberActivationUseCase;
import com.callv2.user.application.member.create.CreateMemberUseCase;
import com.callv2.user.application.member.quota.request.create.CreateRequestQuotaInput;
import com.callv2.user.application.member.quota.request.create.CreateRequestQuotaUseCase;
import com.callv2.user.domain.member.QuotaUnit;
import com.callv2.user.infrastructure.api.MemberAPI;
import com.callv2.user.infrastructure.member.adapter.MemberAdapter;
import com.callv2.user.infrastructure.member.model.CreateMemberRequest;
import com.callv2.user.infrastructure.security.SecurityContext;

@Controller
public class MemberController implements MemberAPI {

    private final CreateMemberUseCase createMemberUseCase;
    private final TogleMemberActivationUseCase togleMemberActivationUseCase;
    private final CreateRequestQuotaUseCase createRequestQuotaUseCase;

    public MemberController(
            final CreateMemberUseCase createMemberUseCase,
            final TogleMemberActivationUseCase togleMemberActivationUseCase,
            final CreateRequestQuotaUseCase createRequestQuotaUseCase) {
        this.createMemberUseCase = createMemberUseCase;
        this.togleMemberActivationUseCase = togleMemberActivationUseCase;
        this.createRequestQuotaUseCase = createRequestQuotaUseCase;
    }

    @Override
    public ResponseEntity<Void> create(CreateMemberRequest request) {
        return ResponseEntity
                .created(URI.create("/members/" + createMemberUseCase.execute(MemberAdapter.adapt(request)).id()))
                .build();
    }

    @Override
    public ResponseEntity<Void> toggleActive(String id, boolean active) {
        togleMemberActivationUseCase.execute(TogleMemberActivationInput.of(id, active));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> requestQuota(long amount, QuotaUnit unit) {

        final String memberId = SecurityContext.getAuthenticatedUser();

        this.createRequestQuotaUseCase.execute(CreateRequestQuotaInput.of(memberId, amount, unit));

        return ResponseEntity.noContent().build();
    }

}
