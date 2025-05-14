package com.callv2.member.application.member.create;

public record CreateMemberInput(String username, String email, String password) {

    public static CreateMemberInput with(
            final String username,
            final String email,
            final String password) {
        return new CreateMemberInput(username, email, password);
    }

    @Override
    public final String toString() {
        return "CreateMemberInput{ username='%s', email='%s', password='***'}".formatted(username, email);
    }

}
