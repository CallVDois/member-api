package com.callv2.member.infrastructure.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.callv2.member.application.member.create.CreateMemberUseCase;
import com.callv2.member.infrastructure.api.MemberAPI;
import com.callv2.member.infrastructure.member.adapter.MemberAdapter;
import com.callv2.member.infrastructure.member.model.CreateMemberRequest;

@Controller
public class MemberController implements MemberAPI {

    private final CreateMemberUseCase createMemberUseCase;

    public MemberController(
            final CreateMemberUseCase createMemberUseCase) {
        this.createMemberUseCase = createMemberUseCase;
    }

    @Override
    public ResponseEntity<Void> create(final CreateMemberRequest request) {
        return ResponseEntity
                .created(URI.create("/members/" + createMemberUseCase.execute(MemberAdapter.adapt(request)).id()))
                .build();
    }

}
