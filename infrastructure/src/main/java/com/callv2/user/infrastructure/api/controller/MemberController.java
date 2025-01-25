package com.callv2.user.infrastructure.api.controller;

import org.springframework.http.ResponseEntity;

import com.callv2.user.infrastructure.api.MemberAPI;
import com.callv2.user.infrastructure.member.model.CreateMemberRequest;

public class MemberController implements MemberAPI {

    @Override
    public ResponseEntity<Void> create(CreateMemberRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

}
