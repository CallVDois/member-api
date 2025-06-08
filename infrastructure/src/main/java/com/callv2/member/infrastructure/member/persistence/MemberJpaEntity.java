package com.callv2.member.infrastructure.member.persistence;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import com.callv2.member.domain.member.entity.Member;
import com.callv2.member.domain.member.entity.MemberID;
import com.callv2.member.domain.member.valueobject.Email;
import com.callv2.member.domain.member.valueobject.Nickname;
import com.callv2.member.domain.member.valueobject.Username;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany
    @JoinTable(name = "member_system", joinColumns = @JoinColumn(name = "member_id"), inverseJoinColumns = @JoinColumn(name = "system_id"))
    private Set<SystemJpaEntity> systems;

    private Instant createdAt;

    private Instant updatedAt;

    private Long synchronizedVersion;

    public MemberJpaEntity(
            final String id,
            final String username,
            final String nickname,
            final String email,
            final Boolean active,
            final Set<SystemJpaEntity> systems,
            final Instant createdAt,
            final Instant updatedAt,
            final Long synchronizedVersion) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.active = active;
        this.systems = systems;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.synchronizedVersion = synchronizedVersion;
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
                systems.stream()
                        .map(SystemJpaEntity::toDomain)
                        .collect(Collectors.toSet()),
                getCreatedAt(),
                getUpdatedAt(),
                getSynchronizedVersion());
    }

    public static MemberJpaEntity fromDomain(final Member member) {
        return new MemberJpaEntity(
                member.getId().getValue(),
                member.getUsername().value(),
                member.getNickname().value(),
                member.getEmail().value(),
                member.isActive(),
                member.getAvailableSystems()
                        .stream()
                        .map(SystemJpaEntity::fromDomain)
                        .collect(Collectors.toSet()),
                member.getCreatedAt(),
                member.getUpdatedAt(),
                member.getSynchronizedVersion());
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

    public Set<SystemJpaEntity> getSystems() {
        return systems;
    }

    public void setSystems(Set<SystemJpaEntity> systems) {
        this.systems = systems;
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

    public Long getSynchronizedVersion() {
        return synchronizedVersion;
    }

    public void setSynchronizedVersion(Long synchronizedVersion) {
        this.synchronizedVersion = synchronizedVersion;
    }

}
