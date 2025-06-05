package com.callv2.member.infrastructure.member;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.valueobject.Email;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.Password;
import com.callv2.member.domain.member.valueobject.PreMember;
import com.callv2.member.domain.member.valueobject.System;
import com.callv2.member.domain.member.valueobject.Username;
import com.callv2.member.infrastructure.external.keycloak.mapper.KeycloakGroupMapper;
import com.callv2.member.infrastructure.external.keycloak.mapper.KeycloakUserMapper;
import com.callv2.member.infrastructure.external.keycloak.model.GroupRepresentation;
import com.callv2.member.infrastructure.external.keycloak.service.KeycloakGroupService;
import com.callv2.member.infrastructure.external.keycloak.service.KeycloakUserService;
import com.callv2.member.infrastructure.filter.FilterService;
import com.callv2.member.infrastructure.member.persistence.MemberJpaEntity;
import com.callv2.member.infrastructure.member.persistence.MemberJpaRepository;

@ExtendWith(MockitoExtension.class)
public class DefaultMemberGatewayTest {

    @InjectMocks
    DefaultMemberGateway gateway;

    @Mock
    FilterService filterService;

    @Mock
    MemberJpaRepository memberJpaRepository;

    @Spy
    KeycloakUserMapper keycloakUserMapper = Mappers.getMapper(KeycloakUserMapper.class);

    @Mock
    KeycloakUserService keycloakUserService;

    @Spy
    KeycloakGroupMapper keycloakGroupMapper = Mappers.getMapper(KeycloakGroupMapper.class);

    @Mock
    KeycloakGroupService keycloakGroupService;

    @Test
    void givenAValidPreMember_whenCallsCreate_thenShouldPersistMemberInKeycloakAndOwnDatabase() {

        final var expectedMemberIdValue = "123";
        final var expectedUserName = Username.of("username");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of(expectedUserName.value());
        final var expectedIsActive = false;
        final var expectedAvailableSystems = Set.<System>of();
        final var expectedPassword = Password.of("password");

        final var preMember = PreMember.with(
                expectedUserName,
                expectedEmail,
                expectedPassword);

        final var userRepresentation = keycloakUserMapper.toUserRepresentation(preMember);

        when(keycloakUserService.createUser(eq(userRepresentation)))
                .thenReturn(expectedMemberIdValue);

        when(memberJpaRepository.save(any(MemberJpaEntity.class)))
                .thenAnswer(returnsFirstArg());

        final var actualMember = assertDoesNotThrow(() -> gateway.create(preMember));

        assertEquals(expectedMemberIdValue, actualMember.getId().getValue());
        assertEquals(expectedUserName, actualMember.getUsername());
        assertEquals(expectedEmail, actualMember.getEmail());
        assertEquals(expectedNickname, actualMember.getNickname());
        assertEquals(expectedIsActive, actualMember.isActive());
        assertEquals(expectedAvailableSystems, actualMember.getAvailableSystems());

        verify(keycloakUserService, times(1)).createUser(eq(userRepresentation));
        verify(keycloakUserService, times(1)).createUser(any());

        verify(memberJpaRepository, times(1)).save(any(MemberJpaEntity.class));

        verify(keycloakUserService, times(0)).deleteUser(any());
    }

    @Test
    void givenAValidPreMember_whenCallsCreateAndKeycloakCreateUserThrowsARandomException_thenShouldNotPersistMemberInKeycloakAndOwnDatabase() {

        final var expectedMemberIdValue = "123";
        final var expectedUserName = Username.of("username");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of(expectedUserName.value());
        final var expectedIsActive = false;
        final var expectedAvailableSystems = Set.<System>of();
        final var expectedPassword = Password.of("password");

        final var expectedRamdomException = new RuntimeException("Keycloak create user error");

        final var preMember = PreMember.with(
                expectedUserName,
                expectedEmail,
                expectedPassword);

        final var userRepresentation = keycloakUserMapper.toUserRepresentation(preMember);

        when(keycloakUserService.createUser(eq(userRepresentation)))
                .thenThrow(expectedRamdomException);

        final var actualException = assertThrows(RuntimeException.class, () -> gateway.create(preMember));

        assertEquals(expectedRamdomException, actualException);

        verify(keycloakUserService, times(1)).createUser(eq(userRepresentation));
        verify(keycloakUserService, times(1)).createUser(any());

        verify(memberJpaRepository, times(0)).save(any());

        verify(keycloakUserService, times(0)).deleteUser(any());
    }

    @Test
    void givenAValidPreMember_whenCallsCreateAndRepositorySaveThrowsARandomException_thenShouldNotPersistMemberInKeycloakAndOwnDatabase() {

        final var expectedMemberIdValue = "123";
        final var expectedUserName = Username.of("username");
        final var expectedEmail = Email.of("email@email.com");
        final var expectedNickname = Nickname.of(expectedUserName.value());
        final var expectedIsActive = false;
        final var expectedAvailableSystems = Set.<System>of();
        final var expectedPassword = Password.of("password");

        final var preMember = PreMember.with(
                expectedUserName,
                expectedEmail,
                expectedPassword);

        final var expectedRamdomException = new RuntimeException("Repository create user error");

        final var userRepresentation = keycloakUserMapper.toUserRepresentation(preMember);

        when(keycloakUserService.createUser(eq(userRepresentation)))
                .thenReturn(expectedMemberIdValue);

        when(memberJpaRepository.save(any(MemberJpaEntity.class)))
                .thenThrow(expectedRamdomException);

        doNothing()
                .when(keycloakUserService)
                .deleteUser(eq(expectedMemberIdValue));

        final var actualException = assertThrows(RuntimeException.class, () -> gateway.create(preMember));

        assertEquals(expectedRamdomException, actualException);

        verify(keycloakUserService, times(1)).createUser(eq(userRepresentation));
        verify(keycloakUserService, times(1)).createUser(any());

        verify(memberJpaRepository, times(1)).save(any());

        verify(keycloakUserService, times(1)).deleteUser(eq(expectedMemberIdValue));
        verify(keycloakUserService, times(1)).deleteUser(any());
    }

    @Test
    void givenAValidMember_whenCallsUpdate_thenShouldUpdateMemberInKeycloakAndOwnDatabase() {

        final var expectedMemberId = MemberID.of("123");
        final var expectedUserName = Username.of("username");
        final var expectedEmail = Email.of("user@member.com");
        final var expectedNickname = Nickname.of("nickname");
        final var expectIsActive = true;
        final var expectedAvailableSystems = Set.of(System.DRIVE, System.MEMBER);
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = expectedCreatedAt.plus(1L, ChronoUnit.HOURS);

        final Member oldMember = Member.with(
                expectedMemberId,
                expectedUserName,
                expectedEmail,
                expectedNickname,
                false,
                Set.of(System.MEMBER),
                expectedCreatedAt,
                expectedCreatedAt);

        final var updatedMember = Member.with(
                expectedMemberId,
                expectedUserName,
                expectedEmail,
                expectedNickname,
                expectIsActive,
                expectedAvailableSystems,
                expectedCreatedAt,
                expectedUpdatedAt);

        final var driveGroupId = "driveGroupId";
        final var driveGroupPath = keycloakGroupMapper.toGroupPath(System.DRIVE);
        final var drivegroupRepresentation = new GroupRepresentation(
                driveGroupId,
                "driveGroupName",
                "driveGroupDescription",
                driveGroupPath,
                null,
                0L,
                List.of(),
                Map.of(),
                List.of(),
                Map.of(),
                Map.of());

        final var memberGroupId = "memberGroupId";
        final var memberGroupPath = keycloakGroupMapper.toGroupPath(System.MEMBER);
        final var memberGroupRepresentation = new GroupRepresentation(
                memberGroupId,
                "memberGroupName",
                "memberGroupDescription",
                memberGroupPath,
                null,
                0L,
                List.of(),
                Map.of(),
                List.of(),
                Map.of(),
                Map.of());

        final var memberJpaEntity = MemberJpaEntity.fromDomain(updatedMember);

        when(memberJpaRepository.findById(eq(expectedMemberId.getValue())))
                .thenReturn(Optional.of(MemberJpaEntity.fromDomain(oldMember)));

        when(keycloakUserMapper.toUserRepresentation(any(Member.class)))
                .thenCallRealMethod();

        when(keycloakGroupMapper.toGroupPaths(anySet()))
                .thenCallRealMethod();

        doNothing()
                .when(keycloakUserService)
                .updateUser(eq(expectedMemberId.getValue()), any());

        when(keycloakGroupService.getGroupByPath(eq(driveGroupPath)))
                .thenReturn(drivegroupRepresentation);

        when(keycloakGroupService.getGroupByPath(eq(memberGroupPath)))
                .thenReturn(memberGroupRepresentation);

        when(keycloakUserService.getGroups(eq(expectedMemberId.getValue())))
                .thenReturn(List.of(memberGroupRepresentation));

        doNothing()
                .when(keycloakUserService)
                .addGroup(eq(expectedMemberId.getValue()), eq(driveGroupId));

        final var actualUpdatedMember = gateway.update(updatedMember);

        assertEquals(expectedMemberId, actualUpdatedMember.getId());
        assertEquals(expectedUserName, actualUpdatedMember.getUsername());
        assertEquals(expectedEmail, actualUpdatedMember.getEmail());
        assertEquals(expectedNickname, actualUpdatedMember.getNickname());
        assertEquals(expectIsActive, actualUpdatedMember.isActive());
        assertEquals(expectedAvailableSystems, actualUpdatedMember.getAvailableSystems());
        assertEquals(expectedCreatedAt, actualUpdatedMember.getCreatedAt());
        assertEquals(expectedUpdatedAt, actualUpdatedMember.getUpdatedAt());

        verify(memberJpaRepository, times(1)).findById(eq(expectedMemberId.getValue()));
        verify(memberJpaRepository, times(1)).findById(any());

        verify(keycloakUserService, times(1)).updateUser(eq(expectedMemberId.getValue()), any());
        verify(keycloakUserService, times(1)).updateUser(any(), any());

        verify(keycloakGroupService, times(1)).getGroupByPath(eq(driveGroupPath));
        verify(keycloakGroupService, times(1)).getGroupByPath(eq(memberGroupPath));
        verify(keycloakGroupService, times(2)).getGroupByPath(any());

        verify(keycloakUserService, times(1)).getGroups(eq(expectedMemberId.getValue()));
        verify(keycloakUserService, times(1)).getGroups(any());

        verify(keycloakUserService, times(0)).deleteGroup(any(), any());

        verify(keycloakUserService, times(1)).addGroup(eq(expectedMemberId.getValue()), eq(driveGroupId));
        verify(keycloakUserService, times(1)).addGroup(any(), any());

        verify(memberJpaRepository, times(1)).save(any(MemberJpaEntity.class));
    }

    @Test
    void givenAValidMemberWithNoKeycloaksDataAltered_whenCallsUpdate_thenShouldUpdateMemberOwnDatabaseButNotInKeycloak() {

        final var expectedMemberId = MemberID.of("123");
        final var expectedUserName = Username.of("username");
        final var expectedEmail = Email.of("user@member.com");
        final var expectedNickname = Nickname.of("new_nickname");
        final var expectIsActive = true;
        final var expectedAvailableSystems = Set.of(System.DRIVE, System.MEMBER);
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = expectedCreatedAt.plus(1L, ChronoUnit.HOURS);

        final Member oldMember = Member.with(
                expectedMemberId,
                expectedUserName,
                expectedEmail,
                Nickname.of("old_Nickname"),
                expectIsActive,
                expectedAvailableSystems,
                expectedCreatedAt,
                expectedCreatedAt);

        final var updatedMember = Member.with(
                expectedMemberId,
                expectedUserName,
                expectedEmail,
                expectedNickname,
                expectIsActive,
                expectedAvailableSystems,
                expectedCreatedAt,
                expectedUpdatedAt);

        final var driveGroupId = "driveGroupId";
        final var driveGroupPath = keycloakGroupMapper.toGroupPath(System.DRIVE);
        final var drivegroupRepresentation = new GroupRepresentation(
                driveGroupId,
                "driveGroupName",
                "driveGroupDescription",
                driveGroupPath,
                null,
                0L,
                List.of(),
                Map.of(),
                List.of(),
                Map.of(),
                Map.of());

        final var memberGroupId = "memberGroupId";
        final var memberGroupPath = keycloakGroupMapper.toGroupPath(System.MEMBER);
        final var memberGroupRepresentation = new GroupRepresentation(
                memberGroupId,
                "memberGroupName",
                "memberGroupDescription",
                memberGroupPath,
                null,
                0L,
                List.of(),
                Map.of(),
                List.of(),
                Map.of(),
                Map.of());

        final var memberJpaEntity = MemberJpaEntity.fromDomain(updatedMember);

        when(memberJpaRepository.findById(eq(expectedMemberId.getValue())))
                .thenReturn(Optional.of(MemberJpaEntity.fromDomain(oldMember)));

        final var actualUpdatedMember = gateway.update(updatedMember);

        assertEquals(expectedMemberId, actualUpdatedMember.getId());
        assertEquals(expectedUserName, actualUpdatedMember.getUsername());
        assertEquals(expectedEmail, actualUpdatedMember.getEmail());
        assertEquals(expectedNickname, actualUpdatedMember.getNickname());
        assertEquals(expectIsActive, actualUpdatedMember.isActive());
        assertEquals(expectedAvailableSystems, actualUpdatedMember.getAvailableSystems());
        assertEquals(expectedCreatedAt, actualUpdatedMember.getCreatedAt());
        assertEquals(expectedUpdatedAt, actualUpdatedMember.getUpdatedAt());

        verify(memberJpaRepository, times(1)).findById(eq(expectedMemberId.getValue()));
        verify(memberJpaRepository, times(1)).findById(any());

        verify(keycloakUserService, times(0)).updateUser(any(), any());
        verify(keycloakGroupService, times(0)).getGroupByPath(any());
        verify(keycloakUserService, times(0)).getGroups(any());
        verify(keycloakUserService, times(0)).deleteGroup(any(), any());
        verify(keycloakUserService, times(0)).addGroup(any(), any());

        verify(memberJpaRepository, times(1)).save(any(MemberJpaEntity.class));
    }

}
