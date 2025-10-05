package com.callv2.member.infrastructure.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.callv2.member.application.member.create.CreateMemberUseCase;
import com.callv2.member.application.member.update.nickname.DefaultChangeNicknameUseCase;
import com.callv2.member.infrastructure.api.MemberAPI;
import com.callv2.member.infrastructure.member.adapter.MemberAdapter;
import com.callv2.member.infrastructure.member.model.ChangeNicknameRequest;
import com.callv2.member.infrastructure.member.model.CreateMemberRequest;

@Controller
public class MemberController implements MemberAPI {

    private final CreateMemberUseCase createMemberUseCase;
    private final DefaultChangeNicknameUseCase changeNicknameUseCase;

    public MemberController(
            final CreateMemberUseCase createMemberUseCase,
            final DefaultChangeNicknameUseCase changeNicknameUseCase) {
        this.createMemberUseCase = createMemberUseCase;
        this.changeNicknameUseCase = changeNicknameUseCase;
    }

    @Override
    public ResponseEntity<Void> create(final CreateMemberRequest request) {
        return ResponseEntity
                .created(URI.create("/members/" + createMemberUseCase.execute(MemberAdapter.adapt(request)).id()))
                .build();
    }

    @Override
    public ResponseEntity<Void> changeNickname(final ChangeNicknameRequest request) {
        changeNicknameUseCase.execute(MemberAdapter.adapt(request));
        return ResponseEntity
                .created(URI.create("/members/change-nickname/" + request.memberId()))
                .build();
    }

}
