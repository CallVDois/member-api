package com.callv2.user.domain.member;

import java.time.Instant;

public record QuotaRequestPreview(
        String memberId,
        String username,
        long amount,
        QuotaUnit unit,
        Instant requestedAt) {

}
