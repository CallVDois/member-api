package com.callv2.user.application.member.quota.request.create;

import com.callv2.user.domain.member.QuotaUnit;

public record CreateRequestQuotaInput(String memberId, Long ammount, QuotaUnit unit) {

}
