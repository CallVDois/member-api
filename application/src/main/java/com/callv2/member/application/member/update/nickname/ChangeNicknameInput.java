package com.callv2.member.application.member.update.nickname;

import java.util.UUID;

public record ChangeNicknameInput(UUID memberId, String nickname) {

    public static ChangeNicknameInput with(
            final UUID memberId,
            final String nickname) {
        return new ChangeNicknameInput(memberId, nickname);
    }

}
