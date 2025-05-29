package com.callv2.member.infrastructure.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.callv2.member.application.member.activation.TogleMemberActivationInput;
import com.callv2.member.application.member.activation.TogleMemberActivationUseCase;
import com.callv2.member.application.member.retrieve.get.GetMemberInput;
import com.callv2.member.application.member.retrieve.get.GetMemberUseCase;
import com.callv2.member.application.member.retrieve.list.ListMembersUseCase;
import com.callv2.member.domain.pagination.Filter;
import com.callv2.member.domain.pagination.Filter.Operator;
import com.callv2.member.domain.pagination.Page;
import com.callv2.member.domain.pagination.Pagination;
import com.callv2.member.domain.pagination.Pagination.Order.Direction;
import com.callv2.member.domain.pagination.SearchQuery;
import com.callv2.member.infrastructure.api.MemberAdminAPI;
import com.callv2.member.infrastructure.filter.adapter.QueryAdapter;
import com.callv2.member.infrastructure.member.model.GetMemberResponse;
import com.callv2.member.infrastructure.member.model.MemberListResponse;
import com.callv2.member.infrastructure.member.presenter.MemberPresenter;

@Controller
public class MemberAdminController implements MemberAdminAPI {

    private final TogleMemberActivationUseCase togleMemberActivationUseCase;
    private final GetMemberUseCase getMemberUseCase;
    private final ListMembersUseCase listMembersUseCase;

    public MemberAdminController(
            final TogleMemberActivationUseCase togleMemberActivationUseCase,
            final GetMemberUseCase getMemberUseCase,
            final ListMembersUseCase listMembersUseCase) {
        this.togleMemberActivationUseCase = togleMemberActivationUseCase;
        this.getMemberUseCase = getMemberUseCase;
        this.listMembersUseCase = listMembersUseCase;
    }

    @Override
    public ResponseEntity<Void> toggleActive(final String id, final boolean active) {
        togleMemberActivationUseCase.execute(TogleMemberActivationInput.of(id, active));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GetMemberResponse> get(String id) {
        return ResponseEntity.ok(MemberPresenter.present(getMemberUseCase.execute(GetMemberInput.from(id))));
    }

    @Override
    public ResponseEntity<Page<MemberListResponse>> list(
            final int page,
            final int perPage,
            final String orderField,
            final Direction orderDirection,
            final Operator filterOperator,
            final List<String> filters) {

        final List<Filter> searchFilters = filters == null ? List.of()
                : filters
                        .stream()
                        .map(QueryAdapter::of)
                        .toList();

        final SearchQuery query = SearchQuery.of(
                Pagination.of(page, perPage, Pagination.Order.of(orderField, orderDirection)),
                filterOperator,
                searchFilters);

        return ResponseEntity.ok(listMembersUseCase.execute(query).map(MemberPresenter::present));

    }

}
