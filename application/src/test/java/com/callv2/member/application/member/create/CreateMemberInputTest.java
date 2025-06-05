package com.callv2.member.application.member.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CreateMemberInputTest {

    @Test
    void givenAValidAttributes_whenCallToString_thenShouldMaskPassword() {

        final String expectedUsername = "testUser";
        final String expectedEmail = "email@email.com";
        final String expectedPassword = "testPass";

        final CreateMemberInput input = CreateMemberInput.with(expectedUsername, expectedEmail, expectedPassword);

        assertEquals(expectedUsername, input.username());
        assertEquals(expectedEmail, input.email());
        assertEquals(expectedPassword, input.password());

        assertTrue(!input.toString().contains(expectedPassword), "Password should be masked in toString output");
    }

}
