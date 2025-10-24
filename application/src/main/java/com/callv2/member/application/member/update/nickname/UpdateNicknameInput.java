package com.callv2.member.application.member.update.nickname;

import java.util.UUID;

public record UpdateNicknameInput(UUID memberId, String nickname) {

    public static UpdateNicknameInput with(
            final UUID memberId,
            final String nickname) {
        return new UpdateNicknameInput(memberId, nickname);
    }

}
