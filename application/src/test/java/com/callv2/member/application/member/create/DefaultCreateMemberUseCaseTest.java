package com.callv2.member.application.member.create;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.member.domain.event.EventDispatcher;
import com.callv2.member.domain.event.EventSource;
import com.callv2.member.domain.exception.ValidationException;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.gateway.MemberGateway;
import com.callv2.member.domain.member.valueobject.Email;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.Password;
import com.callv2.member.domain.member.valueobject.PreMember;
import com.callv2.member.domain.member.valueobject.System;
import com.callv2.member.domain.member.valueobject.Username;

@ExtendWith(MockitoExtension.class)
public class DefaultCreateMemberUseCaseTest {

    @InjectMocks
    DefaultCreateMemberUseCase useCase;

    @Mock
    MemberGateway memberGateway;

    @Mock
    EventDispatcher eventDispatcher;

    @Test
    void givenAValidParams_whenCallsExecute_thenShouldCreateMember() {

        final var expextedUsernameValue = "username";
        final var expectedNicknameValue = "nickname";
        final var expectedEmailValue = "email@email.com";
        final var expectedPasswordValue = "password";

        final var expextedUsername = Username.of(expextedUsernameValue);
        final var expectedNickname = Nickname.of(expectedNicknameValue);
        final var expectedEmail = Email.of(expectedEmailValue);
        final var expectedPassword = Password.of(expectedPasswordValue);

        final var expectedPreMember = PreMember.with(
                expextedUsername,
                expectedEmail,
                expectedPassword);

        final var expectedMemberId = MemberID.of("123");
        final var expectedIsActive = false;
        final var expectedAvailableSystems = Set.<System>of();
        final var expectedCreateAt = Instant.now();
        final var expectedUpdatedAt = expectedCreateAt;

        final var expectedMember = Member.with(
                expectedMemberId,
                expextedUsername,
                expectedEmail,
                expectedNickname,
                expectedIsActive,
                expectedAvailableSystems,
                expectedCreateAt,
                expectedUpdatedAt,
                0L);

        when(memberGateway.create(eq(expectedPreMember)))
                .thenReturn(expectedMember);

        doNothing()
                .when(eventDispatcher)
                .notify(any(EventSource.class));

        final var input = CreateMemberInput.with(
                expextedUsernameValue,
                expectedEmailValue,
                expectedPasswordValue);

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(input));

        assertEquals(expectedMemberId.getValue(), actualOutput.id());

        verify(memberGateway, times(1)).create(eq(expectedPreMember));
        verify(memberGateway, times(1)).create(any(PreMember.class));
        verify(eventDispatcher, times(1)).notify(eq(expectedMember));
        verify(eventDispatcher, times(1)).notify(any(Member.class));
    }

    @Test
    void givenAnInvalidNickname_whenCallsExecute_thenShouldThrowsValidationException() {

        final var expextedUsernameValue = "   ";
        final var expectedEmailValue = "email@email.com";
        final var expectedPasswordValue = "password";

        final var expectedExceptionMessage = "Validation error";
        final var expectedErrorCount = 3;
        final var expectedErrorMessage0 = "'nickname' is required";
        final var expectedErrorMessage1 = "'username' is required";
        final var expectedErrorMessage2 = "'username' cannot contain spaces";

        final var input = CreateMemberInput.with(
                expextedUsernameValue,
                expectedEmailValue,
                expectedPasswordValue);

        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage0, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessage1, actualException.getErrors().get(1).message());
        assertEquals(expectedErrorMessage2, actualException.getErrors().get(2).message());

        verify(memberGateway, times(0)).create(any());

        verify(eventDispatcher, times(0)).notify(any(EventSource.class));
    }

}
