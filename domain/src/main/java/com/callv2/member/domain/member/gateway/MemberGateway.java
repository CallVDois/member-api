package com.callv2.member.domain.member.gateway;

import java.util.Optional;

import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.valueobject.PreMember;
import com.callv2.member.domain.pagination.Page;
import com.callv2.member.domain.pagination.SearchQuery;

public interface MemberGateway {

    Member create(PreMember preMember);

    Page<Member> findAll(SearchQuery searchQuery);

    Optional<Member> findById(MemberID id);

    Member update(Member member);

}
