package com.callv2.member.domain.member.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.callv2.member.domain.member.event.MemberCreatedEvent;
import com.callv2.member.domain.member.valueobject.Email;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.Username;

public class MemberTest {

    @Test
    void givenAValidParams_whenCallsCreate_thenShouldCreateAMember() {
                  
        final var expectedId = MemberID.of("123");
        final var expectedUsername = Username.of("user");
        final var expectedEmail = Email.of("eser@email.com");
        final var expectedNickname = Nickname.of("user_nickname");

        final var expectedEventType = MemberCreatedEvent.class;
        final var expectedEventSource = "MemberAggregate";

        final var actualMember = assertDoesNotThrow(() -> Member.create(
                expectedId,
                expectedUsername,
                expectedEmail,
                expectedNickname));

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedUsername, actualMember.getUsername());
        assertEquals(expectedEmail, actualMember.getEmail());
        assertEquals(expectedNickname, actualMember.getNickname());
        assertFalse(actualMember.isActive());
        assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
        assertTrue(actualMember.getAvailableSystems().isEmpty());

        final var createdEvent = actualMember.nextEvent();
        assertTrue(createdEvent.isPresent());
        assertEquals(expectedEventType, createdEvent.get().getClass());
        assertEquals(expectedEventSource, createdEvent.get().source());
        assertEquals(MemberCreatedEvent.Data.of(actualMember), createdEvent.get().data());

        final var anotherEvent = actualMember.nextEvent();
        assertFalse(anotherEvent.isPresent(), "There should be no more events after the first one");
    }

}
