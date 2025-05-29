package com.callv2.member.application.member.create;

public record CreateMemberOutput(String id) {

    public static CreateMemberOutput with(String id) {
        return new CreateMemberOutput(id);
    }

}
