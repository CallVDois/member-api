package com.callv2.user.infrastructure.member;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.callv2.user.domain.member.Email;
import com.callv2.user.domain.member.Member;
import com.callv2.user.domain.member.MemberGateway;
import com.callv2.user.domain.member.PreMember;
import com.callv2.user.domain.member.Username;
import com.callv2.user.infrastructure.member.persistence.MemberJpaRepository;

@Component
public class DefaultMemberGateway implements MemberGateway {

    private final MemberJpaRepository memberJpaRepository;

    public DefaultMemberGateway(final MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Member create(PreMember preMember) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
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
