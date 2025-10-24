package com.callv2.member.application.member.create;

import java.util.UUID;

public record CreateMemberOutput(UUID id) {

    public static CreateMemberOutput with(UUID id) {
        return new CreateMemberOutput(id);
    }

}
