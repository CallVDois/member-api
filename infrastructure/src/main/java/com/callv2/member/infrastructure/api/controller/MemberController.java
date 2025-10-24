package com.callv2.member.infrastructure.api.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.callv2.member.application.member.create.CreateMemberUseCase;
import com.callv2.member.application.member.update.nickname.UpdateNicknameUseCase;
import com.callv2.member.infrastructure.api.MemberAPI;
import com.callv2.member.infrastructure.member.adapter.MemberAdapter;
import com.callv2.member.infrastructure.member.model.UpdateNicknameRequest;
import com.callv2.member.infrastructure.member.model.CreateMemberRequest;
import com.callv2.member.infrastructure.security.SecurityContext;

@Controller
public class MemberController implements MemberAPI {

    private final CreateMemberUseCase createMemberUseCase;
    private final UpdateNicknameUseCase updateNicknameUseCase;

    public MemberController(
            final CreateMemberUseCase createMemberUseCase,
            final UpdateNicknameUseCase updateNicknameUseCase) {
        this.createMemberUseCase = createMemberUseCase;
        this.updateNicknameUseCase = updateNicknameUseCase;
    }

    @Override
    public ResponseEntity<Void> create(final CreateMemberRequest request) {
        return ResponseEntity
                .created(URI.create("/members/" + createMemberUseCase.execute(MemberAdapter.adapt(request)).id()))
                .build();
    }

    @Override
    public ResponseEntity<Void> updateNickname(final UpdateNicknameRequest request) {
        final UUID memberId = SecurityContext.getAuthenticatedUser();
        updateNicknameUseCase.execute(MemberAdapter.adapt(memberId, request));
        return ResponseEntity
                .noContent()
                .build();
    }

}
