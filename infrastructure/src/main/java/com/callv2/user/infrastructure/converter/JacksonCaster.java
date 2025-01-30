package com.callv2.user.infrastructure.converter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.callv2.user.infrastructure.configuration.mapper.Mapper;

@Component
public final class JacksonCaster implements Caster {

    private static final ObjectMapper mapper = Mapper.mapper();

    private JacksonCaster() {
    }

    @Override
    public <T> T cast(Object value, Class<T> targetType) {
        return mapper.convertValue(value, targetType);
    }

}
