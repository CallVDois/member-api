package com.callv2.member.infrastructure.member;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.gateway.MemberGateway;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.PreMember;
import com.callv2.member.domain.pagination.Page;
import com.callv2.member.domain.pagination.SearchQuery;
import com.callv2.member.infrastructure.external.keycloak.mapper.KeycloakGroupMapper;
import com.callv2.member.infrastructure.external.keycloak.mapper.KeycloakUserMapper;
import com.callv2.member.infrastructure.external.keycloak.model.GroupRepresentation;
import com.callv2.member.infrastructure.external.keycloak.service.KeycloakGroupService;
import com.callv2.member.infrastructure.external.keycloak.service.KeycloakUserService;
import com.callv2.member.infrastructure.filter.FilterService;
import com.callv2.member.infrastructure.filter.adapter.QueryAdapter;
import com.callv2.member.infrastructure.member.persistence.MemberJpaEntity;
import com.callv2.member.infrastructure.member.persistence.MemberJpaRepository;

public class DefaultMemberGateway implements MemberGateway {

    private final FilterService filterService;

    private final MemberJpaRepository memberJpaRepository;

    private final KeycloakUserMapper keycloakUserMapper;
    private final KeycloakUserService clientKeycloakUserService;
    private final KeycloakUserService tokenKeycloakUserService;

    private final KeycloakGroupMapper keycloakGroupMapper;
    private final KeycloakGroupService keycloakGroupService;

    public DefaultMemberGateway(
            final FilterService filterService,
            final MemberJpaRepository memberJpaRepository,
            final KeycloakUserMapper keycloakUserMapper,
            final KeycloakUserService clientKeycloakUserService,
            final KeycloakUserService tokenKeycloakUserService,
            final KeycloakGroupMapper keycloakGroupMapper,
            final KeycloakGroupService keycloakGroupService) {
        this.filterService = filterService;
        this.memberJpaRepository = memberJpaRepository;

        this.keycloakUserMapper = keycloakUserMapper;
        this.clientKeycloakUserService = clientKeycloakUserService;
        this.tokenKeycloakUserService = tokenKeycloakUserService;

        this.keycloakGroupMapper = keycloakGroupMapper;
        this.keycloakGroupService = keycloakGroupService;
    }

    @Override
    public Member create(final PreMember preMember) {

        final String memberId = this.clientKeycloakUserService
                .createUser(keycloakUserMapper.toUserRepresentation(preMember));

        try {
            return save(
                    Member.create(
                            MemberID.of(memberId),
                            preMember.username(),
                            preMember.email(),
                            Nickname.of(preMember.username().value())));
        } catch (Exception e) {
            this.clientKeycloakUserService.deleteUser(memberId);
            throw e;
        }
    }

    @Override
    public Optional<Member> findById(final MemberID id) {
        return this.memberJpaRepository
                .findById(id.getValue())
                .map(MemberJpaEntity::toDomain);
    }

    @Override
    public Page<Member> findAll(SearchQuery searchQuery) {

        final var page = QueryAdapter.of(searchQuery.pagination());

        final Specification<MemberJpaEntity> specification = filterService.buildSpecification(
                MemberJpaEntity.class,
                searchQuery.filterMethod(),
                searchQuery.filters());

        final org.springframework.data.domain.Page<MemberJpaEntity> pageResult = this.memberJpaRepository
                .findAll(Specification.where(specification), page);

        return new Page<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                pageResult.map(MemberJpaEntity::toDomain).toList());

    }

    @Override
    public Member update(final Member member) {

        final Optional<Member> actualMember = findById(member.getId());

        // These two filters need to be separates because of a division inside the
        // Keycloak API where a user's group needs to be updated separetely
        actualMember
                .filter(actMember -> needsKeycloakUserDataUpdate(actMember, member))
                .ifPresent(actMember -> performKeycloakUserDataUpdate(member));

        actualMember
                .filter(actMember -> needsKeycloakUserGroupUpdate(actMember, member))
                .ifPresent(actMember -> performKeycloakUserGroupUpdate(member));

        return save(member);
    }

    private Member save(final Member member) {
        memberJpaRepository
                .save(MemberJpaEntity.fromDomain(member));

        return member;
    }

    private boolean needsKeycloakUserDataUpdate(final Member actualMember, final Member newMember) {
        return !(actualMember.getEmail().equals(newMember.getEmail())
                && actualMember.getUsername().equals(newMember.getUsername())
                && actualMember.isActive() == newMember.isActive());
    }

    private void performKeycloakUserDataUpdate(final Member member) {
        this.tokenKeycloakUserService
                .updateUser(member.getId().getValue(), keycloakUserMapper.toUserRepresentation(member));
    }

    private boolean needsKeycloakUserGroupUpdate(final Member actualMember, final Member newMember) {
        return !(actualMember.getAvailableSystems().equals(newMember.getAvailableSystems()));
    }

    private void performKeycloakUserGroupUpdate(final Member member) {
        final String userId = member.getId().getValue();
        final Set<String> newUserGroupsPaths = this.keycloakGroupMapper.toGroupPaths(member.getAvailableSystems());

        final List<String> newUserGroupIds = newUserGroupsPaths
                .stream()
                .map(this.keycloakGroupService::getGroupByPath)
                .map(GroupRepresentation::id)
                .toList();

        final List<String> actualUserGroupIds = this.clientKeycloakUserService.getGroups(userId)
                .stream()
                .map(GroupRepresentation::id)
                .toList();

        final List<String> groupsToRemove = actualUserGroupIds
                .stream()
                .filter(groupId -> !newUserGroupIds.contains(groupId))
                .toList();

        final List<String> groupsToAdd = newUserGroupIds
                .stream()
                .filter(groupId -> !actualUserGroupIds.contains(groupId))
                .toList();

        groupsToRemove.forEach(group -> this.clientKeycloakUserService.deleteGroup(userId, group));
        groupsToAdd.forEach(group -> this.clientKeycloakUserService.addGroup(userId, group));
    }

}
