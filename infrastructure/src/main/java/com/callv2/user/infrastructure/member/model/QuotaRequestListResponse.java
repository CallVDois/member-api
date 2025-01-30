package com.callv2.user.infrastructure.member.model;

import java.time.Instant;

import com.callv2.user.domain.member.QuotaUnit;

public record QuotaRequestListResponse(
        String memberId,
        String username,
        long amount,
        QuotaUnit unit,
        Instant requestedAt) {

}
