package com.callv2.member.application.member.retrieve.list;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.gateway.MemberGateway;
import com.callv2.member.domain.member.valueobject.Email;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.Username;
import com.callv2.member.domain.pagination.Page;
import com.callv2.member.domain.pagination.Pagination;
import com.callv2.member.domain.pagination.SearchQuery;
import com.callv2.member.domain.pagination.Filter.Operator;
import com.callv2.member.domain.pagination.Pagination.Order;
import com.callv2.member.domain.pagination.Pagination.Order.Direction;
import com.callv2.member.domain.member.valueobject.System;

@ExtendWith(MockitoExtension.class)
public class DefaultListMembersUseCaseTest {

    @InjectMocks
    DefaultListMembersUseCase useCase;

    @Mock
    MemberGateway memberGateway;

    @Test
    void givenAValidSearchQuery_whenCallsExecute_thenShouldReturnMembers() {

        final var expectedMember1 = Member.with(
                MemberID.of("1"),
                Username.of("user1"),
                Email.of("email1@email.com"),
                Nickname.of("nickname1"),
                true,
                Set.of(System.DRIVE, System.MEMBER),
                Instant.now(),
                Instant.now());

        final var expectedMember2 = Member.with(
                MemberID.of("2"),
                Username.of("user2"),
                Email.of("email2@email.com"),
                Nickname.of("nickname2"),
                true,
                Set.of(System.DRIVE),
                Instant.now(),
                Instant.now());

        final var expectedMembers = List.of(expectedMember1, expectedMember2);

        final var expectedPageMembers = new Page<Member>(
                0,
                10,
                1,
                expectedMembers.size(),
                expectedMembers);

        when(memberGateway.findAll(any()))
                .thenReturn(expectedPageMembers);

        final var input = SearchQuery.of(
                Pagination.of(0, 2, Order.of("createdAt", Direction.DESC)),
                Operator.AND,
                List.of());

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(input));

        assertEquals(expectedPageMembers.currentPage(), actualOutput.currentPage());
        assertEquals(expectedPageMembers.perPage(), actualOutput.perPage());
        assertEquals(expectedPageMembers.total(), actualOutput.total());
        assertEquals(expectedPageMembers.items().size(), actualOutput.items().size());

        assertEquals(MemberListOutput.class, actualOutput.items().get(0).getClass());

        assertEquals(expectedMember1.getId().getValue(), actualOutput.items().get(0).id());
        assertEquals(expectedMember1.getUsername().value(), actualOutput.items().get(0).username());
        assertEquals(expectedMember1.getEmail().value(), actualOutput.items().get(0).email());
        assertEquals(expectedMember1.getNickname().value(), actualOutput.items().get(0).nickname());
        assertEquals(expectedMember1.isActive(), actualOutput.items().get(0).active());

        assertEquals(expectedMember2.getId().getValue(), actualOutput.items().get(1).id());
        assertEquals(expectedMember2.getUsername().value(), actualOutput.items().get(1).username());
        assertEquals(expectedMember2.getEmail().value(), actualOutput.items().get(1).email());
        assertEquals(expectedMember2.getNickname().value(), actualOutput.items().get(1).nickname());
        assertEquals(expectedMember2.isActive(), actualOutput.items().get(1).active());

    }

}
