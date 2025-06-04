package com.callv2.member.application.member.activation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.member.domain.event.EventDispatcher;
import com.callv2.member.domain.exception.NotFoundException;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.gateway.MemberGateway;
import com.callv2.member.domain.member.valueobject.Email;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.System;
import com.callv2.member.domain.member.valueobject.Username;

@ExtendWith(MockitoExtension.class)
public class DefaultTogleMemberActivationUseCaseTest {

    @InjectMocks
    DefaultTogleMemberActivationUseCase useCase;

    @Mock
    MemberGateway memberGateway;

    @Mock
    EventDispatcher eventDispatcher;

    @Test
    void givenAnActiveTrueInput_whenCallsExecute_thenShouldActivateMember() {

        final var expectedIdValue = "123";
        final var expectedIsActive = true;

        final var expectedMemberId = MemberID.of(expectedIdValue);
        final var expectedUsername = Username.of("username");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of("nickname");
        final var expectedAvailableSystems = Set.of(System.DRIVE, System.MEMBER);
        final var expectedCreateAt = Instant.now();
        final var expectedUpdatedAt = expectedCreateAt;

        final var expectedMember = Member.with(
                expectedMemberId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                false,
                expectedAvailableSystems,
                expectedCreateAt,
                expectedUpdatedAt);

        final var input = TogleMemberActivationInput.of(expectedIdValue, expectedIsActive);

        when(memberGateway.findById(expectedMemberId))
                .thenReturn(Optional.of(expectedMember));

        when(memberGateway.update(argThat(member -> {

            assertEquals(expectedMemberId, member.getId());
            assertEquals(expectedUsername, member.getUsername());
            assertEquals(expectedEmail, member.getEmail());
            assertEquals(expectedNickname, member.getNickname());
            assertEquals(expectedIsActive, member.isActive());
            assertEquals(expectedAvailableSystems, member.getAvailableSystems());
            assertEquals(expectedCreateAt, member.getCreatedAt());
            assertTrue(expectedCreateAt.isBefore(member.getUpdatedAt()));
            assertTrue(expectedUpdatedAt.isBefore(member.getUpdatedAt()));

            return true;

        }))).thenAnswer(returnsFirstArg());

        assertDoesNotThrow(() -> useCase.execute(input));

        verify(memberGateway, times(1)).findById(expectedMemberId);
        verify(memberGateway, times(1)).update(argThat(member -> {

            assertEquals(expectedMemberId, member.getId());
            assertEquals(expectedUsername, member.getUsername());
            assertEquals(expectedEmail, member.getEmail());
            assertEquals(expectedNickname, member.getNickname());
            assertEquals(expectedIsActive, member.isActive());
            assertEquals(expectedAvailableSystems, member.getAvailableSystems());
            assertEquals(expectedCreateAt, member.getCreatedAt());
            assertTrue(expectedCreateAt.isBefore(member.getUpdatedAt()));
            assertTrue(expectedUpdatedAt.isBefore(member.getUpdatedAt()));

            return true;

        }));
    }

    @Test
    void givenAnActiveFalseInput_whenCallsExecute_thenShouldDeactivateMember() {

        final var expectedIdValue = "123";
        final var expectedIsActive = false;

        final var expectedMemberId = MemberID.of(expectedIdValue);
        final var expectedUsername = Username.of("username");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of("nickname");
        final var expectedAvailableSystems = Set.of(System.DRIVE, System.MEMBER);
        final var expectedCreateAt = Instant.now();
        final var expectedUpdatedAt = expectedCreateAt;

        final var expectedMember = Member.with(
                expectedMemberId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                true,
                expectedAvailableSystems,
                expectedCreateAt,
                expectedUpdatedAt);

        final var input = TogleMemberActivationInput.of(expectedIdValue, expectedIsActive);

        when(memberGateway.findById(expectedMemberId))
                .thenReturn(Optional.of(expectedMember));

        when(memberGateway.update(argThat(member -> {

            assertEquals(expectedMemberId, member.getId());
            assertEquals(expectedUsername, member.getUsername());
            assertEquals(expectedEmail, member.getEmail());
            assertEquals(expectedNickname, member.getNickname());
            assertEquals(expectedIsActive, member.isActive());
            assertEquals(expectedAvailableSystems, member.getAvailableSystems());
            assertEquals(expectedCreateAt, member.getCreatedAt());
            assertTrue(expectedCreateAt.isBefore(member.getUpdatedAt()));
            assertTrue(expectedUpdatedAt.isBefore(member.getUpdatedAt()));

            return true;

        }))).thenAnswer(returnsFirstArg());

        assertDoesNotThrow(() -> useCase.execute(input));

        verify(memberGateway, times(1)).findById(expectedMemberId);
        verify(memberGateway, times(1)).update(argThat(member -> {

            assertEquals(expectedMemberId, member.getId());
            assertEquals(expectedUsername, member.getUsername());
            assertEquals(expectedEmail, member.getEmail());
            assertEquals(expectedNickname, member.getNickname());
            assertEquals(expectedIsActive, member.isActive());
            assertEquals(expectedAvailableSystems, member.getAvailableSystems());
            assertEquals(expectedCreateAt, member.getCreatedAt());
            assertTrue(expectedCreateAt.isBefore(member.getUpdatedAt()));
            assertTrue(expectedUpdatedAt.isBefore(member.getUpdatedAt()));

            return true;

        }));
    }

    @Test
    void givenAnNonExistentMemberId_whenCallsExecute_thenShouldThrowsNotFoundException() {

        final var expectedIdValue = "123";
        final var expectedIsActive = false;

        final var expectedMemberId = MemberID.of(expectedIdValue);

        final var expectedExceptionMessage = "Member with id '123' not found";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Member with id '123' not found";

        final var input = TogleMemberActivationInput.of(expectedIdValue, expectedIsActive);

        when(memberGateway.findById(eq(expectedMemberId)))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(memberGateway, times(1)).findById(expectedMemberId);
        verify(memberGateway, times(0)).update(any());
    }

}
