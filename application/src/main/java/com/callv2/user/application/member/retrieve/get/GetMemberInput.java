package com.callv2.user.application.member.retrieve.get;

public record GetMemberInput(String id) {

    public static GetMemberInput from(String id) {
        return new GetMemberInput(id);
    }

}
