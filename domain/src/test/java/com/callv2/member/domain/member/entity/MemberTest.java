package com.callv2.member.domain.member.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.callv2.member.domain.member.event.MemberCreatedEvent;
import com.callv2.member.domain.member.event.MemberUpdatedEvent;
import com.callv2.member.domain.member.valueobject.Email;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.Username;
import com.callv2.member.domain.member.valueobject.System;

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

    @Test
    void givenAMemberWithNoAvailableSystems_whenCallsUpdateAvailableSystem_thenShouldAddTheSystem() {

        final var expectedId = MemberID.of("123");
        final var expectedUsername = Username.of("user");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of("user_nickname");
        final var expectedCreateAt = Instant.now();
        final var expectedUpdatedAt = expectedCreateAt;
        final var expectedMember = Member.with(
                expectedId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                false,
                Set.of(),
                expectedCreateAt,
                expectedUpdatedAt);

        final var expectedSystems = Set.of(System.DRIVE);

        final var actualMember = assertDoesNotThrow(() -> expectedMember.updateAvailableSystems(expectedSystems));

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedUsername, actualMember.getUsername());
        assertEquals(expectedEmail, actualMember.getEmail());
        assertEquals(expectedNickname, actualMember.getNickname());
        assertFalse(actualMember.isActive());
        assertEquals(expectedCreateAt, actualMember.getCreatedAt());
        assertTrue(actualMember.getAvailableSystems().containsAll(expectedSystems));
        assertEquals(expectedSystems.size(), actualMember.getAvailableSystems().size());
        assertTrue(actualMember.getUpdatedAt().isAfter(expectedUpdatedAt));

        final var updatedEvent = actualMember.nextEvent();
        assertTrue(updatedEvent.isPresent());
        assertEquals(MemberUpdatedEvent.class, updatedEvent.get().getClass());
        assertEquals("MemberAggregate", updatedEvent.get().source());
        assertEquals(MemberUpdatedEvent.Data.of(actualMember), updatedEvent.get().data());
        final var anotherEvent = actualMember.nextEvent();
        assertFalse(anotherEvent.isPresent());
    }

    @Test
    void givenAnAlreadyAvailableSystems_whenCallsUpdateAvailableSystem_thenShouldDoNothing() {

        final var expectedId = MemberID.of("123");
        final var expectedUsername = Username.of("user");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of("user_nickname");
        final var expectedCreateAt = Instant.now();
        final var expectedSystems = Set.of(System.DRIVE);
        final var expectedUpdatedAt = expectedCreateAt;
        final var expectedMember = Member.with(
                expectedId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                false,
                expectedSystems,
                expectedCreateAt,
                expectedUpdatedAt);

        final var inputSystems = Set.of(System.DRIVE);
        final var actualMember = assertDoesNotThrow(() -> expectedMember.updateAvailableSystems(inputSystems));

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedUsername, actualMember.getUsername());
        assertEquals(expectedEmail, actualMember.getEmail());
        assertEquals(expectedNickname, actualMember.getNickname());
        assertFalse(actualMember.isActive());
        assertEquals(expectedCreateAt, actualMember.getCreatedAt());
        assertTrue(actualMember.getAvailableSystems().containsAll(expectedSystems));
        assertEquals(expectedSystems.size(), actualMember.getAvailableSystems().size());
        assertEquals(expectedUpdatedAt, actualMember.getUpdatedAt());

        assertFalse(actualMember.nextEvent().isPresent());
    }

    @Test
    void givenANullSystems_whenCallsUpdateAvailableSystem_thenShouldDoNothing() {

        final var expectedId = MemberID.of("123");
        final var expectedUsername = Username.of("user");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of("user_nickname");
        final var expectedCreateAt = Instant.now();
        final var expectedSystems = Set.of(System.DRIVE);
        final var expectedUpdatedAt = expectedCreateAt;
        final var expectedMember = Member.with(
                expectedId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                false,
                expectedSystems,
                expectedCreateAt,
                expectedUpdatedAt);

        final var actualMember = assertDoesNotThrow(() -> expectedMember.updateAvailableSystems(null));

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedUsername, actualMember.getUsername());
        assertEquals(expectedEmail, actualMember.getEmail());
        assertEquals(expectedNickname, actualMember.getNickname());
        assertFalse(actualMember.isActive());
        assertEquals(expectedCreateAt, actualMember.getCreatedAt());
        assertTrue(actualMember.getAvailableSystems().containsAll(expectedSystems));
        assertEquals(expectedSystems.size(), actualMember.getAvailableSystems().size());
        assertEquals(expectedUpdatedAt, actualMember.getUpdatedAt());

        assertFalse(actualMember.nextEvent().isPresent());
    }

    @Test
    void givenAEmptySystems_whenCallsUpdateAvailableSystems_thenShouldClearAvailableSystems() {

        final var expectedId = MemberID.of("123");
        final var expectedUsername = Username.of("user");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of("user_nickname");
        final var expectedCreateAt = Instant.now();
        final var expectedSystems = Set.of();
        final var expectedUpdatedAt = expectedCreateAt;
        final var expectedMember = Member.with(
                expectedId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                false,
                Set.of(System.DRIVE, System.MEMBER),
                expectedCreateAt,
                expectedUpdatedAt);

        final var input = Set.<System>of();
        final var actualMember = assertDoesNotThrow(() -> expectedMember.updateAvailableSystems(input));

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedUsername, actualMember.getUsername());
        assertEquals(expectedEmail, actualMember.getEmail());
        assertEquals(expectedNickname, actualMember.getNickname());
        assertFalse(actualMember.isActive());
        assertEquals(expectedCreateAt, actualMember.getCreatedAt());
        assertTrue(actualMember.getAvailableSystems().containsAll(expectedSystems));
        assertEquals(expectedSystems.size(), actualMember.getAvailableSystems().size());
        assertTrue(actualMember.getUpdatedAt().isAfter(actualMember.getCreatedAt()));

        final var updatedEvent = actualMember.nextEvent();
        assertTrue(updatedEvent.isPresent());
        assertEquals(MemberUpdatedEvent.class, updatedEvent.get().getClass());
        assertEquals("MemberAggregate", updatedEvent.get().source());
        assertEquals(MemberUpdatedEvent.Data.of(actualMember), updatedEvent.get().data());
        final var anotherEvent = actualMember.nextEvent();
        assertFalse(anotherEvent.isPresent());
    }

    @Test
    void givenAnInactiveMember_whenCallsActivate_thenShouldActivateTheMember() {

        final var expectedId = MemberID.of("123");
        final var expectedUsername = Username.of("user");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of("user_nickname");
        final var expectedCreateAt = Instant.now();
        final var expectedIsActive = true;
        final var expectedSystems = Set.of(System.DRIVE, System.MEMBER);
        final var expectedUpdatedAt = expectedCreateAt;
        final var expectedMember = Member.with(
                expectedId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                false,
                expectedSystems,
                expectedCreateAt,
                expectedUpdatedAt);

        final var actualMember = assertDoesNotThrow(() -> expectedMember.activate());

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedUsername, actualMember.getUsername());
        assertEquals(expectedEmail, actualMember.getEmail());
        assertEquals(expectedNickname, actualMember.getNickname());
        assertEquals(expectedIsActive, actualMember.isActive());
        assertEquals(expectedCreateAt, actualMember.getCreatedAt());
        assertTrue(actualMember.getAvailableSystems().containsAll(expectedSystems));
        assertEquals(expectedSystems.size(), actualMember.getAvailableSystems().size());
        assertTrue(actualMember.getUpdatedAt().isAfter(expectedUpdatedAt));
        final var updatedEvent = actualMember.nextEvent();
        assertTrue(updatedEvent.isPresent());
        assertEquals(MemberUpdatedEvent.class, updatedEvent.get().getClass());
        assertEquals("MemberAggregate", updatedEvent.get().source());
        assertEquals(MemberUpdatedEvent.Data.of(actualMember), updatedEvent.get().data());
        final var anotherEvent = actualMember.nextEvent();
        assertFalse(anotherEvent.isPresent());
    }

    @Test
    void givenAnActiveMember_whenCallsDeactivate_thenShouldInactivateTheMember() {

        final var expectedId = MemberID.of("123");
        final var expectedUsername = Username.of("user");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of("user_nickname");
        final var expectedCreateAt = Instant.now();
        final var expectedIsActive = false;
        final var expectedSystems = Set.of(System.DRIVE, System.MEMBER);
        final var expectedUpdatedAt = expectedCreateAt;
        final var expectedMember = Member.with(
                expectedId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                true,
                expectedSystems,
                expectedCreateAt,
                expectedUpdatedAt);

        final var actualMember = assertDoesNotThrow(() -> expectedMember.deactivate());

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedUsername, actualMember.getUsername());
        assertEquals(expectedEmail, actualMember.getEmail());
        assertEquals(expectedNickname, actualMember.getNickname());
        assertEquals(expectedIsActive, actualMember.isActive());
        assertEquals(expectedCreateAt, actualMember.getCreatedAt());
        assertTrue(actualMember.getAvailableSystems().containsAll(expectedSystems));
        assertEquals(expectedSystems.size(), actualMember.getAvailableSystems().size());
        assertTrue(actualMember.getUpdatedAt().isAfter(expectedUpdatedAt));
        final var updatedEvent = actualMember.nextEvent();
        assertTrue(updatedEvent.isPresent());
        assertEquals(MemberUpdatedEvent.class, updatedEvent.get().getClass());
        assertEquals("MemberAggregate", updatedEvent.get().source());
        assertEquals(MemberUpdatedEvent.Data.of(actualMember), updatedEvent.get().data());
        final var anotherEvent = actualMember.nextEvent();
        assertFalse(anotherEvent.isPresent());
    }

    @Test
    void givenAnInactiveMember_whenCallsDeactivate_thenShouldDoNothing() {

        final var expectedId = MemberID.of("123");
        final var expectedUsername = Username.of("user");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of("user_nickname");
        final var expectedCreateAt = Instant.now();
        final var expectedIsActive = false;
        final var expectedSystems = Set.of(System.DRIVE, System.MEMBER);
        final var expectedUpdatedAt = expectedCreateAt;
        final var expectedMember = Member.with(
                expectedId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                false,
                expectedSystems,
                expectedCreateAt,
                expectedUpdatedAt);

        final var actualMember = assertDoesNotThrow(() -> expectedMember.deactivate());

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedUsername, actualMember.getUsername());
        assertEquals(expectedEmail, actualMember.getEmail());
        assertEquals(expectedNickname, actualMember.getNickname());
        assertEquals(expectedIsActive, actualMember.isActive());
        assertEquals(expectedCreateAt, actualMember.getCreatedAt());
        assertTrue(actualMember.getAvailableSystems().containsAll(expectedSystems));
        assertEquals(expectedSystems.size(), actualMember.getAvailableSystems().size());
        assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
        final var anotherEvent = actualMember.nextEvent();
        assertFalse(anotherEvent.isPresent());
    }

    @Test
    void givenAnActiveMember_whenCallsActivate_thenShouldDoNothing() {

        final var expectedId = MemberID.of("123");
        final var expectedUsername = Username.of("user");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of("user_nickname");
        final var expectedCreateAt = Instant.now();
        final var expectedIsActive = true;
        final var expectedSystems = Set.of(System.DRIVE, System.MEMBER);
        final var expectedUpdatedAt = expectedCreateAt;
        final var expectedMember = Member.with(
                expectedId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                true,
                expectedSystems,
                expectedCreateAt,
                expectedUpdatedAt);

        final var actualMember = assertDoesNotThrow(() -> expectedMember.activate());

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedUsername, actualMember.getUsername());
        assertEquals(expectedEmail, actualMember.getEmail());
        assertEquals(expectedNickname, actualMember.getNickname());
        assertEquals(expectedIsActive, actualMember.isActive());
        assertEquals(expectedCreateAt, actualMember.getCreatedAt());
        assertTrue(actualMember.getAvailableSystems().containsAll(expectedSystems));
        assertEquals(expectedSystems.size(), actualMember.getAvailableSystems().size());
        assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
        final var anotherEvent = actualMember.nextEvent();
        assertFalse(anotherEvent.isPresent());
    }

}
