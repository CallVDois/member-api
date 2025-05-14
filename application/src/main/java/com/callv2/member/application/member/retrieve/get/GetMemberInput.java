package com.callv2.member.application.member.retrieve.get;

public record GetMemberInput(String id) {

    public static GetMemberInput from(String id) {
        return new GetMemberInput(id);
    }

}
