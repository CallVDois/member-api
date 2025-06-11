package com.callv2.member.application.member.update.system;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.member.domain.event.EventDispatcher;
import com.callv2.member.domain.event.EventSource;
import com.callv2.member.domain.exception.NotFoundException;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.gateway.MemberGateway;
import com.callv2.member.domain.member.valueobject.Email;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.System;
import com.callv2.member.domain.member.valueobject.Username;

@ExtendWith(MockitoExtension.class)
public class DefaultUpdateMemberSystemAccessUseCaseTest {

    @InjectMocks
    DefaultUpdateMemberSystemAccessUseCase useCase;

    @Mock
    MemberGateway memberGateway;

    @Mock
    EventDispatcher eventDispatcher;

    @Test
    void givenAValidParams_whenCallsExecute_thenShouldUpdateMemberSystemAccess() {

        final var expectedId = "123";
        final var expectedSystems = Set.of(System.DRIVE, System.MEMBER);

        final var expectedMemberId = MemberID.of(expectedId);
        final var expectedUsername = Username.of("username");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of("nickname");
        final var expectIsActive = true;
        final var expectedCreateAt = Instant.now();
        final var expectedUpdatedAt = expectedCreateAt;

        final var expectedMember = Member.with(
                expectedMemberId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                expectIsActive,
                Set.of(),
                expectedCreateAt,
                expectedUpdatedAt,
                0L);

        assertTrue(expectedMember.nextEvent().isEmpty());
        assertTrue(expectedMember.getAvailableSystems().isEmpty());

        when(memberGateway.findById(expectedMemberId))
                .thenReturn(Optional.of(expectedMember));

        when(memberGateway.update(expectedMember))
                .thenAnswer(returnsFirstArg());

        doNothing()
                .when(eventDispatcher)
                .notify(any(EventSource.class));

        final var input = UpdateMemberSystemAccessInput.of(expectedId, expectedSystems);

        assertDoesNotThrow(() -> useCase.execute(input));

        verify(memberGateway, times(1)).findById(eq(expectedMemberId));
        verify(memberGateway, times(1)).update(any());
        verify(eventDispatcher, times(1)).notify(argThat(((ArgumentMatcher<Member>) member -> {

            assertEquals(expectedMemberId, member.getId());
            assertEquals(expectedUsername, member.getUsername());
            assertEquals(expectedEmail, member.getEmail());
            assertEquals(expectedNickname, member.getNickname());
            assertEquals(expectIsActive, member.isActive());
            assertTrue(member.getAvailableSystems().containsAll(expectedSystems));
            assertEquals(expectedSystems.size(), member.getAvailableSystems().size());
            assertEquals(expectedCreateAt, member.getCreatedAt());
            assertTrue(member.getUpdatedAt().isAfter(expectedCreateAt));

            return true;
        })));

    }

    @Test
    void givenAnNonExistentMemberId_whenCallsExecute_thenShouldThrowsNotFoundException() {

        final var expectedId = "123";
        final var expectedSystems = Set.of(System.DRIVE, System.MEMBER);
        final var expectedMemberId = MemberID.of(expectedId);
        final var expectedExceptionMessage = "Member with id '123' not found";
        final var expectedErrorMessage = "Member with id '123' not found";
        final var expectedErrorCount = 1;

        when(memberGateway.findById(expectedMemberId))
                .thenReturn(Optional.empty());

        final var input = UpdateMemberSystemAccessInput.of(expectedId, expectedSystems);

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(memberGateway, times(1)).findById(any());
        verify(memberGateway, times(1)).findById(eq(expectedMemberId));
        verify(memberGateway, times(0)).update(any());
        verify(eventDispatcher, times(0)).notify(any(EventSource.class));

    }

}
