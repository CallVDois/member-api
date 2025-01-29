package com.callv2.user.domain.member;

import java.time.Instant;
import java.util.Optional;

import com.callv2.user.domain.AggregateRoot;
import com.callv2.user.domain.exception.ValidationException;
import com.callv2.user.domain.validation.ValidationHandler;
import com.callv2.user.domain.validation.handler.Notification;

public class Member extends AggregateRoot<MemberID> {

    private Username username;
    private Email email;
    private Nickname nickname;

    private boolean active;

    private Quota quota;
    private Optional<QuotaRequest> quotaRequest;

    private Instant createdAt;
    private Instant updatedAt;

    private Member(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname,
            final boolean active,
            final Quota quota,
            final QuotaRequest quotaRequest,
            final Instant createdAt,
            final Instant updatedAt) {
        super(id);
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.active = active;
        this.quota = quota;
        this.quotaRequest = Optional.ofNullable(quotaRequest);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Member create(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname) {

        final Instant now = Instant.now();
        final Quota quota = Quota.of(0, QuotaUnit.BYTE);

        return new Member(id, username, email, nickname, false, quota, null, now, now);
    }

    public static Member with(
            final MemberID id,
            final Username username,
            final Email email,
            final Nickname nickname,
            final boolean active,
            final Quota quota,
            final QuotaRequest quotaRequest,
            final Instant createdAt,
            final Instant updatedAt) {
        return new Member(id, username, email, nickname, active, quota, quotaRequest, createdAt, updatedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new MemberValidator(this, handler).validate();
    }

    public Member activate() {

        if (this.active)
            return this;

        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Member deactivate() {

        if (!this.active)
            return this;

        this.active = false;
        this.quota = Quota.of(0, QuotaUnit.BYTE);
        this.updatedAt = Instant.now();
        return this;
    }

    public Member requestQuota(final Quota quota) {

        if (quota == null)
            return this;

        final QuotaRequest actualQuotaRequest = this.quotaRequest.orElse(null);
        final QuotaRequest newQuotaRequest = QuotaRequest.of(quota, Instant.now());
        this.quotaRequest = Optional.ofNullable(newQuotaRequest);

        final Notification notification = Notification.create();
        this.validate(notification);

        if (notification.hasError()) {
            this.quotaRequest = Optional.ofNullable(actualQuotaRequest);
            throw ValidationException.with("Request Quota Error", notification);
        }

        this.updatedAt = Instant.now();
        return this;
    }

    public Member approveQuotaRequest() {

        if (this.quotaRequest.isEmpty())
            return this;

        this.quota = this.quotaRequest
                .map(QuotaRequest::quota)
                .orElse(Quota.of(0, QuotaUnit.BYTE));

        this.quotaRequest = Optional.empty();

        this.updatedAt = Instant.now();

        return this;
    }

    public Username getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public boolean isActive() {
        return active;
    }

    public Quota getQuota() {
        return quota;
    }

    public Optional<QuotaRequest> getQuotaRequest() {
        return quotaRequest;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
