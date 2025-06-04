package com.callv2.member.application.member.retrieve.get;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import com.callv2.member.domain.exception.NotFoundException;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.gateway.MemberGateway;
import com.callv2.member.domain.member.valueobject.Email;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.System;
import com.callv2.member.domain.member.valueobject.Username;

@ExtendWith(MockitoExtension.class)
public class DefaultGetMemberUseCaseTest {

    @InjectMocks
    DefaultGetMemberUseCase useCase;

    @Mock
    MemberGateway memberGateway;

    @Test
    void givenAnExistentMemberId_whenCallsExecute_thenShouldReturnMember() {

        final var expectedIdValue = "123";
        final var expectedMemberId = MemberID.of(expectedIdValue);
        final var expectedUsername = Username.of("username");
        final var expectedEmail = Email.of("email@eail.com");
        final var expectedNickname = Nickname.of("nickname");
        final var expectedIsActive = true;
        final var expectedAvailableSystems = Set.of(System.DRIVE, System.MEMBER);
        final var expectedCreateAt = Instant.now();
        final var expectedUpdatedAt = expectedCreateAt;

        final var expectedMember = Member.with(
                expectedMemberId,
                expectedUsername,
                expectedEmail,
                expectedNickname,
                expectedIsActive,
                expectedAvailableSystems,
                expectedCreateAt,
                expectedUpdatedAt);

        when(memberGateway.findById(eq(expectedMemberId)))
                .thenReturn(Optional.of(expectedMember));

        final var input = GetMemberInput.from(expectedIdValue);
        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(input));

        assertEquals(expectedIdValue, actualOutput.id());
        assertEquals(expectedUsername.value(), actualOutput.username());
        assertEquals(expectedEmail.value(), actualOutput.email());
        assertEquals(expectedNickname.value(), actualOutput.nickname());
        assertEquals(expectedIsActive, actualOutput.active());
        assertEquals(expectedAvailableSystems, actualOutput.availableSystems());
        assertEquals(expectedCreateAt, actualOutput.createdAt());
        assertEquals(expectedUpdatedAt, actualOutput.updatedAt());

        verify(memberGateway, times(1)).findById(eq(expectedMemberId));
        verify(memberGateway, times(1)).findById(any());
    }

    @Test
    void givenAnNonExistentMemberId_whenCallsExecute_thenShouldThorwsNotFoundException() {

        final var expectedIdValue = "123";
        final var expectedMemberId = MemberID.of(expectedIdValue);

        final var expectedExceptionMessage = "Member with id '123' not found";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Member with id '123' not found";

        when(memberGateway.findById(eq(expectedMemberId)))
                .thenReturn(Optional.empty());

        final var input = GetMemberInput.from(expectedIdValue);
        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(memberGateway, times(1)).findById(eq(expectedMemberId));
        verify(memberGateway, times(1)).findById(any());
    }

}
