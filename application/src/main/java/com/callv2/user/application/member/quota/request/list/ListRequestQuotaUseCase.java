package com.callv2.user.application.member.quota.request.list;

import com.callv2.user.application.UseCase;
import com.callv2.user.domain.pagination.Pagination;
import com.callv2.user.domain.pagination.SearchQuery;

public abstract class ListRequestQuotaUseCase extends UseCase<SearchQuery, Pagination<RequestQuotaListOutput>> {

}
