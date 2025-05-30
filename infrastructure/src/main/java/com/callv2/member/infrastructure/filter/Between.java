package com.callv2.member.infrastructure.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.callv2.member.domain.pagination.Filter;
import com.callv2.member.infrastructure.converter.Caster;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class Between extends SpecificationFilter {

    public Between(final Caster caster) {
        super(caster);
    }

    @Override
    public Filter.Type filterType() {
        return Filter.Type.BETWEEN;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> Specification<T> buildSpecification(Filter filter) {

        validateFilter(filter);

        return (root, query, criteriaBuilder) -> between(
                (Class<Comparable>) root.get(filter.field()).getJavaType(),
                root,
                criteriaBuilder,
                filter);
    }

    private <T extends Comparable<T>, R> Predicate between(
            final Class<T> type,
            final Root<R> root,
            final CriteriaBuilder criteriaBuilder,
            final Filter filter) {
        return criteriaBuilder.between(
                root.get(filter.field()).as(type),
                cast(filter.value(), type),
                cast(filter.valueToCompare(), type));
    }

    private void validateFilter(final Filter filter) {
        if (filter.value() == null || filter.valueToCompare() == null)
            throw new IllegalArgumentException("Value cannot be null");

        if (!Filter.Type.BETWEEN.equals(filter.type()))
            throw new IllegalArgumentException("Filter type must be BETWEEN");
    }

}
