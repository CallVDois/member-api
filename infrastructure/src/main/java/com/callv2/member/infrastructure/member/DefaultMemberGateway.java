package com.callv2.member.infrastructure.member;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import com.callv2.member.domain.exception.NotFoundException;
import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.gateway.MemberGateway;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.PreMember;
import com.callv2.member.domain.pagination.Page;
import com.callv2.member.domain.pagination.SearchQuery;
import com.callv2.member.infrastructure.external.keycloak.mapper.KeycloakUserMapper;
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

    public DefaultMemberGateway(
            final FilterService filterService,
            final MemberJpaRepository memberJpaRepository,
            final KeycloakUserMapper keycloakUserMapper,
            final KeycloakUserService clientKeycloakUserService,
            final KeycloakUserService tokenKeycloakUserService) {
        this.filterService = filterService;
        this.memberJpaRepository = memberJpaRepository;
        this.keycloakUserMapper = keycloakUserMapper;
        this.clientKeycloakUserService = clientKeycloakUserService;
        this.tokenKeycloakUserService = tokenKeycloakUserService;
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

        if (performKeycloakUpdate(member))
            this.tokenKeycloakUserService
                    .updateUser(keycloakUserMapper.toUserRepresentation(member), member.getId().getValue());

        return save(member);
    }

    private Member save(final Member member) {
        memberJpaRepository
                .save(MemberJpaEntity.fromDomain(member));

        return member;
    }

    private boolean performKeycloakUpdate(final Member member) {
        final MemberJpaEntity memberJpa = this.memberJpaRepository
                .findById(member.getId().getValue())
                .orElseThrow(() -> NotFoundException.with(Member.class, member.getId().getValue()));

        return !(memberJpa.getEmail().equals(member.getEmail().value())
                && memberJpa.getUsername().equals(member.getUsername().value())
                && memberJpa.getActive() == member.isActive());

    }

}
