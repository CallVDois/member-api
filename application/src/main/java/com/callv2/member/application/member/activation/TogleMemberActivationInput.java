package com.callv2.member.application.member.activation;

import java.util.UUID;

public record TogleMemberActivationInput(UUID memberId, Boolean active) {

    public static TogleMemberActivationInput of(UUID memberId, Boolean active) {
        return new TogleMemberActivationInput(memberId, active);
    }

}
