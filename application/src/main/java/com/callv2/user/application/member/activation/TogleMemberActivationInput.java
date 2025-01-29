package com.callv2.user.application.member.activation;

public record TogleMemberActivationInput(String memberId, Boolean active) {

    public static TogleMemberActivationInput of(String memberId, Boolean active) {
        return new TogleMemberActivationInput(memberId, active);
    }

}
