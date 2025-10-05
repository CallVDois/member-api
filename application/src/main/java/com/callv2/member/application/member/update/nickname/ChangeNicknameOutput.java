package com.callv2.member.application.member.update.nickname;

import java.util.UUID;

public record ChangeNicknameOutput(UUID id) {

    public static ChangeNicknameOutput with(UUID id) {
        return new ChangeNicknameOutput(id);
    }

}
