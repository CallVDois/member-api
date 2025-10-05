package com.callv2.member.application.member.retrieve.get;

import java.util.UUID;

public record GetMemberInput(UUID id) {

    public static GetMemberInput from(UUID id) {
        return new GetMemberInput(id);
    }

}
