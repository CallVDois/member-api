package com.callv2.member.infrastructure.member.persistence;

import java.time.Instant;

import com.callv2.member.domain.member.Email;
import com.callv2.member.domain.member.Member;
import com.callv2.member.domain.member.MemberID;
import com.callv2.member.domain.member.Nickname;
import com.callv2.member.domain.member.Username;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    private Instant createdAt;

    private Instant updatedAt;

    public MemberJpaEntity(
            final String id,
            final String username,
            final String nickname,
            final String email,
            final Boolean active,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public MemberJpaEntity() {
    }

    public Member toDomain() {
        return Member.with(
                MemberID.of(getId()),
                Username.of(getUsername()),
                Email.of(getEmail()),
                Nickname.of(getNickname()),
                getActive(),
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
