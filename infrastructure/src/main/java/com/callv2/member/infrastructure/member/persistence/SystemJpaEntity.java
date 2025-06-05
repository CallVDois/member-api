package com.callv2.member.infrastructure.member.persistence;

import com.callv2.member.domain.member.valueobject.System;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "systems")
public class SystemJpaEntity {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "system", nullable = false, unique = true)
    private System system;

    public SystemJpaEntity() {
    }

    public SystemJpaEntity(System system) {
        this.system = system;
    }

    public System toDomain() {
        return this.system;
    }

    public static SystemJpaEntity fromDomain(System system) {
        return new SystemJpaEntity(system);
    }

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

}
