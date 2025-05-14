package com.callv2.member.infrastructure.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.callv2.member.domain.pagination.Filter;
import com.callv2.member.infrastructure.converter.Caster;

@Component
public class Like extends SpecificationFilter {

    public Like(final Caster caster) {
        super(caster);
    }

    @Override
    public Filter.Type filterType() {
        return Filter.Type.LIKE;
    }

    @Override
    public <T> Specification<T> buildSpecification(Filter filter) {

        validateFilter(filter);

        return (root, query, criteriaBuilder) -> criteriaBuilder
                .like(root.get(filter.field()), "%" + filter.value() + "%");
    }

    private void validateFilter(final Filter filter) {
        if (filter.value() == null)
            throw new IllegalArgumentException("Value cannot be null");

        if (!Filter.Type.LIKE.equals(filter.type()))
            throw new IllegalArgumentException("Filter type must be LIKE");
    }

}
