package com.callv2.member.application.member.retrieve.list;

import com.callv2.member.application.UseCase;
import com.callv2.member.domain.pagination.Page;
import com.callv2.member.domain.pagination.SearchQuery;

public abstract class ListMembersUseCase extends UseCase<SearchQuery, Page<MemberListOutput>> {

}
