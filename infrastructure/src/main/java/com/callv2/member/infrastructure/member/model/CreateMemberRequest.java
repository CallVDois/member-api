package com.callv2.member.infrastructure.member.model;

public record CreateMemberRequest(String username, String email, String password) {

    @Override
    public String toString() {
        return "CreateMemberRequest [username=" + username + ", email=" + email + ", password=****]";
    }

}
