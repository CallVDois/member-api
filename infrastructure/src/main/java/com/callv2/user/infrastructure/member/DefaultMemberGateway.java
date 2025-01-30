package com.callv2.user.infrastructure.member;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import com.callv2.user.domain.exception.NotFoundException;
import com.callv2.user.domain.member.Member;
import com.callv2.user.domain.member.MemberGateway;
import com.callv2.user.domain.member.MemberID;
import com.callv2.user.domain.member.Nickname;
import com.callv2.user.domain.member.PreMember;
import com.callv2.user.domain.member.QuotaRequestPreview;
import com.callv2.user.domain.pagination.Pagination;
import com.callv2.user.domain.pagination.SearchQuery;
import com.callv2.user.infrastructure.filter.FilterService;
import com.callv2.user.infrastructure.filter.adapter.QueryAdapter;
import com.callv2.user.infrastructure.keycloak.mapper.KeycloakUserMapper;
import com.callv2.user.infrastructure.keycloak.service.KeycloakUserService;
import com.callv2.user.infrastructure.member.persistence.MemberJpaEntity;
import com.callv2.user.infrastructure.member.persistence.MemberJpaRepository;

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
    public Member update(final Member member) {

        if (performKeycloakUpdate(member))
            this.tokenKeycloakUserService
                    .updateUser(keycloakUserMapper.toUserRepresentation(member), member.getId().getValue());

        return save(member);
    }

    private Member save(final Member member) {
        return memberJpaRepository
                .save(MemberJpaEntity.fromDomain(member))
                .toDomain();
    }

    private boolean performKeycloakUpdate(final Member member) {
        final MemberJpaEntity memberJpa = this.memberJpaRepository
                .findById(member.getId().getValue())
                .orElseThrow(() -> NotFoundException.with(Member.class, member.getId().getValue()));

        return !(memberJpa.getEmail().equals(member.getEmail().value())
                && memberJpa.getUsername().equals(member.getUsername().value())
                && memberJpa.getActive() == member.isActive());

    }

    @Override
    public Pagination<QuotaRequestPreview> findAllQuotaRequests(SearchQuery searchQuery) {
        final var page = QueryAdapter.of(searchQuery);

        final Specification<MemberJpaEntity> specification = filterService.buildSpecification(
                MemberJpaEntity.class,
                searchQuery.filterMethod(),
                searchQuery.filters());

        final Page<QuotaRequestPreview> pageResult = this.memberJpaRepository
                .findAllQuotaRequests(Specification.where(specification), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                pageResult.toList());
    }

}
