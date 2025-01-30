package com.callv2.user.infrastructure.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.callv2.user.application.member.activation.TogleMemberActivationInput;
import com.callv2.user.application.member.activation.TogleMemberActivationUseCase;
import com.callv2.user.application.member.create.CreateMemberUseCase;
import com.callv2.user.application.member.quota.request.approve.ApproveRequestQuotaInput;
import com.callv2.user.application.member.quota.request.approve.ApproveRequestQuotaUseCase;
import com.callv2.user.application.member.quota.request.create.CreateRequestQuotaInput;
import com.callv2.user.application.member.quota.request.create.CreateRequestQuotaUseCase;
import com.callv2.user.application.member.quota.request.list.ListRequestQuotaUseCase;
import com.callv2.user.domain.member.QuotaUnit;
import com.callv2.user.domain.pagination.Pagination;
import com.callv2.user.domain.pagination.SearchQuery;
import com.callv2.user.domain.pagination.SearchQuery.FilterMethod;
import com.callv2.user.domain.pagination.SearchQuery.Order.Direction;
import com.callv2.user.infrastructure.api.MemberAPI;
import com.callv2.user.infrastructure.filter.adapter.QueryAdapter;
import com.callv2.user.infrastructure.member.adapter.MemberAdapter;
import com.callv2.user.infrastructure.member.model.CreateMemberRequest;
import com.callv2.user.infrastructure.member.model.QuotaRequestListResponse;
import com.callv2.user.infrastructure.member.presenter.MemberPresenter;
import com.callv2.user.infrastructure.security.SecurityContext;

@Controller
public class MemberController implements MemberAPI {

    private final CreateMemberUseCase createMemberUseCase;
    private final TogleMemberActivationUseCase togleMemberActivationUseCase;
    private final CreateRequestQuotaUseCase createRequestQuotaUseCase;
    private final ApproveRequestQuotaUseCase approveRequestQuotaUseCase;
    private final ListRequestQuotaUseCase listRequestQuotaUseCase;

    public MemberController(
            final CreateMemberUseCase createMemberUseCase,
            final TogleMemberActivationUseCase togleMemberActivationUseCase,
            final CreateRequestQuotaUseCase createRequestQuotaUseCase,
            final ApproveRequestQuotaUseCase approveRequestQuotaUseCase,
            final ListRequestQuotaUseCase listRequestQuotaUseCase) {
        this.createMemberUseCase = createMemberUseCase;
        this.togleMemberActivationUseCase = togleMemberActivationUseCase;
        this.createRequestQuotaUseCase = createRequestQuotaUseCase;
        this.approveRequestQuotaUseCase = approveRequestQuotaUseCase;
        this.listRequestQuotaUseCase = listRequestQuotaUseCase;
    }

    @Override
    public ResponseEntity<Void> create(final CreateMemberRequest request) {
        return ResponseEntity
                .created(URI.create("/members/" + createMemberUseCase.execute(MemberAdapter.adapt(request)).id()))
                .build();
    }

    @Override
    public ResponseEntity<Void> toggleActive(final String id, final boolean active) {
        togleMemberActivationUseCase.execute(TogleMemberActivationInput.of(id, active));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> requestQuota(final long amount, final QuotaUnit unit) {

        final String memberId = SecurityContext.getAuthenticatedUser();

        this.createRequestQuotaUseCase.execute(CreateRequestQuotaInput.of(memberId, amount, unit));

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> approveQuotaRequest(final String id, final boolean approved) {
        this.approveRequestQuotaUseCase.execute(ApproveRequestQuotaInput.of(id, approved));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Pagination<QuotaRequestListResponse>> list(
            final int page,
            final int perPage,
            final String orderField,
            final Direction orderDirection,
            final FilterMethod filterMethod,
            final List<String> filters) {

        final List<SearchQuery.Filter> searchFilters = filters == null ? List.of()
                : filters
                        .stream()
                        .map(QueryAdapter::of)
                        .toList();

        final SearchQuery query = SearchQuery.of(
                page,
                perPage,
                SearchQuery.Order.of(orderField, orderDirection),
                filterMethod,
                searchFilters);

        return ResponseEntity.ok(listRequestQuotaUseCase.execute(query).map(MemberPresenter::present));

    }

}
