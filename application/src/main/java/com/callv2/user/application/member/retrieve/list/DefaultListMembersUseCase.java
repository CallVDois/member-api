package com.callv2.user.application.member.retrieve.list;

import java.util.Objects;

import com.callv2.user.domain.member.MemberGateway;
import com.callv2.user.domain.pagination.Page;
import com.callv2.user.domain.pagination.SearchQuery;

public class DefaultListMembersUseCase extends ListMembersUseCase {

    private final MemberGateway memberGateway;

    public DefaultListMembersUseCase(final MemberGateway memberGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
    }

    @Override
    public Page<MemberListOutput> execute(final SearchQuery searchQuery) {
        return memberGateway
                .findAll(searchQuery)
                .map(MemberListOutput::from);
    }

}
