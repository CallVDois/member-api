package com.callv2.user.domain.member;

import java.util.Optional;

public interface MemberGateway {

    Member create(PreMember preMember);

    Optional<Member> findById(MemberID id);

    Member update(Member member);

}
