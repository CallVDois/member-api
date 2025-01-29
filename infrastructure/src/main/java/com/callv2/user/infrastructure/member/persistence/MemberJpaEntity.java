package com.callv2.user.infrastructure.member.persistence;

import java.time.Instant;

import com.callv2.user.domain.member.Email;
import com.callv2.user.domain.member.Member;
import com.callv2.user.domain.member.MemberID;
import com.callv2.user.domain.member.Nickname;
import com.callv2.user.domain.member.Quota;
import com.callv2.user.domain.member.QuotaRequest;
import com.callv2.user.domain.member.QuotaUnit;
import com.callv2.user.domain.member.Username;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Member")
@Table(name = "members")
public class MemberJpaEntity {

    @Id
    private String id;

    private String username;

    private String nickname;

    private String email;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Long quotaAmmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuotaUnit quotaUnit;

    private Long quotaRequestAmmount;

    @Enumerated(EnumType.STRING)
    private QuotaUnit quotaRequestUnit;

    private Instant quotaRequestedAt;

    private Instant createdAt;

    private Instant updatedAt;

    public MemberJpaEntity(
            final String id,
            final String username,
            final String nickname,
            final String email,
            final Boolean active,
            final Long quotaAmmount,
            final QuotaUnit quotaUnit,
            final Long quotaRequestAmmount,
            final QuotaUnit quotaRequestUnit,
            final Instant quotaRequestedAt,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.active = active;
        this.quotaAmmount = quotaAmmount;
        this.quotaUnit = quotaUnit;
        this.quotaRequestAmmount = quotaRequestAmmount;
        this.quotaRequestUnit = quotaRequestUnit;
        this.quotaRequestedAt = quotaRequestedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public MemberJpaEntity() {
    }

    public Member toDomain() {

        QuotaRequest quotaRequest = null;
        if (getQuotaRequestAmmount() != null && getQuotaRequestUnit() != null && getQuotaRequestedAt() != null)
            quotaRequest = QuotaRequest.of(
                    Quota.of(getQuotaRequestAmmount(), getQuotaRequestUnit()),
                    getQuotaRequestedAt());

        return Member.with(
                MemberID.of(getId()),
                Username.of(getUsername()),
                Email.of(getEmail()),
                Nickname.of(getNickname()),
                getActive(),
                Quota.of(getQuotaAmmount(), getQuotaUnit()),
                quotaRequest,
                getCreatedAt(),
                getUpdatedAt());
    }

    public static MemberJpaEntity fromDomain(final Member member) {
        return new MemberJpaEntity(
                member.getId().getValue(),
                member.getUsername().value(),
                member.getNickname().value(),
                member.getEmail().value(),
                member.isActive(),
                member.getQuota().ammount(),
                member.getQuota().unit(),
                member.getQuotaRequest().map(QuotaRequest::quota).map(Quota::ammount).orElse(null),
                member.getQuotaRequest().map(QuotaRequest::quota).map(Quota::unit).orElse(null),
                member.getQuotaRequest().map(QuotaRequest::requesteddAt).orElse(null),
                member.getCreatedAt(),
                member.getUpdatedAt());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getQuotaAmmount() {
        return quotaAmmount;
    }

    public void setQuotaAmmount(Long quotaAmmount) {
        this.quotaAmmount = quotaAmmount;
    }

    public QuotaUnit getQuotaUnit() {
        return quotaUnit;
    }

    public void setQuotaUnit(QuotaUnit quotaUnit) {
        this.quotaUnit = quotaUnit;
    }

    public Long getQuotaRequestAmmount() {
        return quotaRequestAmmount;
    }

    public void setQuotaRequestAmmount(Long quotaRequestAmmount) {
        this.quotaRequestAmmount = quotaRequestAmmount;
    }

    public QuotaUnit getQuotaRequestUnit() {
        return quotaRequestUnit;
    }

    public void setQuotaRequestUnit(QuotaUnit quotaRequestUnit) {
        this.quotaRequestUnit = quotaRequestUnit;
    }

    public Instant getQuotaRequestedAt() {
        return quotaRequestedAt;
    }

    public void setQuotaRequestedAt(Instant quotaRequestedAt) {
        this.quotaRequestedAt = quotaRequestedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}
