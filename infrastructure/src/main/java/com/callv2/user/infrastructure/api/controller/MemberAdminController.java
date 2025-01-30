package com.callv2.user.infrastructure.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.callv2.user.application.member.activation.TogleMemberActivationInput;
import com.callv2.user.application.member.activation.TogleMemberActivationUseCase;
import com.callv2.user.application.member.retrieve.get.GetMemberInput;
import com.callv2.user.application.member.retrieve.get.GetMemberUseCase;
import com.callv2.user.infrastructure.api.MemberAdminAPI;
import com.callv2.user.infrastructure.member.model.GetMemberResponse;
import com.callv2.user.infrastructure.member.presenter.MemberPresenter;

@Controller
public class MemberAdminController implements MemberAdminAPI {

    private final TogleMemberActivationUseCase togleMemberActivationUseCase;
    private final GetMemberUseCase getMemberUseCase;

    public MemberAdminController(
            final TogleMemberActivationUseCase togleMemberActivationUseCase,
            final GetMemberUseCase getMemberUseCase) {
        this.togleMemberActivationUseCase = togleMemberActivationUseCase;
        this.getMemberUseCase = getMemberUseCase;
    }

    @Override
    public ResponseEntity<Void> toggleActive(final String id, final boolean active) {
        togleMemberActivationUseCase.execute(TogleMemberActivationInput.of(id, active));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GetMemberResponse> get(String id) {
        return ResponseEntity.ok(MemberPresenter.present(getMemberUseCase.execute(GetMemberInput.from(id))));
    }

}
