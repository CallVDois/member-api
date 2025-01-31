package com.callv2.user.infrastructure.member.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, String> {

    Page<MemberJpaEntity> findAll(Specification<MemberJpaEntity> whereClause, Pageable page);

}
