package com.callv2.user.infrastructure.member.presenter;

import com.callv2.user.application.member.quota.request.list.RequestQuotaListOutput;
import com.callv2.user.infrastructure.member.model.QuotaRequestListResponse;

public interface MemberPresenter {

    static QuotaRequestListResponse present(RequestQuotaListOutput output) {
        return new QuotaRequestListResponse(
                output.memberId(),
                output.username(),
                output.amount(),
                output.unit(),
                output.requestedAt());
    }

}
