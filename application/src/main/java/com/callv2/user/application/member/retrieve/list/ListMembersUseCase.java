package com.callv2.user.application.member.retrieve.list;

import com.callv2.user.application.UseCase;
import com.callv2.user.domain.pagination.Page;
import com.callv2.user.domain.pagination.SearchQuery;

public abstract class ListMembersUseCase extends UseCase<SearchQuery, Page<MemberListOutput>> {

}
