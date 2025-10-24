package com.callv2.member.infrastructure.member.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, UUID> {

    Page<MemberJpaEntity> findAll(Specification<MemberJpaEntity> whereClause, Pageable page);

}
