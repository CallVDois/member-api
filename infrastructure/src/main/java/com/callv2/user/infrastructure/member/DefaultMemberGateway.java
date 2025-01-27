package com.callv2.user.infrastructure.member;

import java.util.Optional;

import org.springframework.stereotype.Component;

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

@Component
public class DefaultMemberGateway implements MemberGateway {

    private final MemberJpaRepository memberJpaRepository;
    private final KeycloakUserService keycloakUserService;
    private final KeycloakUserMapper keycloakUserMapper;

    public DefaultMemberGateway(
            final MemberJpaRepository memberJpaRepository,
            final KeycloakUserService keycloakUserService,
            final KeycloakUserMapper keycloakUserMapper) {
        this.memberJpaRepository = memberJpaRepository;
        this.keycloakUserService = keycloakUserService;
        this.keycloakUserMapper = keycloakUserMapper;
    }

    @Override
    public Member create(PreMember preMember) {

        final String memberId = this.keycloakUserService.createUser(keycloakUserMapper.toUserRepresentation(preMember));

        try {
            return memberJpaRepository
                    .save(MemberJpaEntity.fromDomain(
                            Member.create(
                                    MemberID.of(memberId),
                                    preMember.username(),
                                    preMember.email(),
                                    Nickname.of(preMember.username().value()))))
                    .toDomain();
        } catch (Exception e) {
            this.keycloakUserService.deleteUser(memberId);
            throw e;
        }
    }

    @Override
    public Optional<Member> findByUsername(Username username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }

}
