package com.callv2.user.application.member.quota.request.list;

import java.time.Instant;

import com.callv2.user.domain.member.QuotaRequestPreview;
import com.callv2.user.domain.member.QuotaUnit;

public record RequestQuotaListOutput(
        String memberId,
        String username,
        long amount,
        QuotaUnit unit,
        Instant requestedAt) {

    public static RequestQuotaListOutput from(final QuotaRequestPreview quotaRequestPreview) {
        return new RequestQuotaListOutput(
                quotaRequestPreview.memberId(),
                quotaRequestPreview.username(),
                quotaRequestPreview.amount(),
                quotaRequestPreview.unit(),
                quotaRequestPreview.requestedAt());
    }

}
