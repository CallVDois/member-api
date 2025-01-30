package com.callv2.user.domain.member;

import java.util.Optional;

import com.callv2.user.domain.pagination.Pagination;
import com.callv2.user.domain.pagination.SearchQuery;

public interface MemberGateway {

    Member create(PreMember preMember);

    Optional<Member> findById(MemberID id);

    Optional<Member> findByUsername(Username username);

    Optional<Member> findByEmail(Email email);

    Member update(Member member);

    Pagination<QuotaRequestPreview> findAllQuotaRequests(final SearchQuery searchQuery);

}
