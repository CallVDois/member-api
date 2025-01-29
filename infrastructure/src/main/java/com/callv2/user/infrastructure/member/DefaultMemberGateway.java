package com.callv2.user.infrastructure.member;

import java.util.Optional;

import com.callv2.user.domain.member.Email;
import com.callv2.user.domain.member.Member;
import com.callv2.user.domain.member.MemberGateway;
import com.callv2.user.domain.member.MemberID;
import com.callv2.user.domain.member.Nickname;
import com.callv2.user.domain.member.PreMember;
import com.callv2.user.domain.member.Username;
import com.callv2.user.infrastructure.keycloak.mapper.KeycloakUserMapper;
import com.callv2.user.infrastructure.keycloak.service.KeycloakUserService;
import com.callv2.user.infrastructure.member.persistence.MemberJpaEntity;
import com.callv2.user.infrastructure.member.persistence.MemberJpaRepository;

public class DefaultMemberGateway implements MemberGateway {

    private final MemberJpaRepository memberJpaRepository;
    private final KeycloakUserMapper keycloakUserMapper;

    private final KeycloakUserService clientKeycloakUserService;
    private final KeycloakUserService tokenKeycloakUserService;

    public DefaultMemberGateway(
            final MemberJpaRepository memberJpaRepository,
            final KeycloakUserMapper keycloakUserMapper,
            final KeycloakUserService clientKeycloakUserService,
            final KeycloakUserService tokenKeycloakUserService) {
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
    public Optional<Member> findByUsername(final Username username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    @Override
    public Optional<Member> findByEmail(final Email email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }

    @Override
    public Member update(final Member member) {
        tokenKeycloakUserService.updateUser(keycloakUserMapper.toUserRepresentation(member), member.getId().getValue());
        return save(member);
    }

    private Member save(final Member member) {
        return memberJpaRepository
                .save(MemberJpaEntity.fromDomain(member))
                .toDomain();
    }

}
