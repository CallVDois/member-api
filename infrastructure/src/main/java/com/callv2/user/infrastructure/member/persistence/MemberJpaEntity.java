package com.callv2.user.infrastructure.member.persistence;

import java.time.Instant;

import com.callv2.user.domain.member.Email;
import com.callv2.user.domain.member.Member;
import com.callv2.user.domain.member.MemberID;
import com.callv2.user.domain.member.Nickname;
import com.callv2.user.domain.member.Username;

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

    private Instant createdAt;

    private Instant updatedAt;

    public MemberJpaEntity() {
    }

    private MemberJpaEntity(
            final String id,
            final String username,
            final String nickname,
            final String email,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Member toDomain() {
        return Member.with(
                MemberID.of(id),
                Username.of(username),
                Email.of(email),
                Nickname.of(nickname),
                createdAt,
                updatedAt);
    }

    public static MemberJpaEntity fromDomain(final Member member) {
        return new MemberJpaEntity(
                member.getId().getValue(),
                member.getUsername().value(),
                member.getNickname().value(),
                member.getEmail().value(),
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
