package com.callv2.user.infrastructure.converter;

@FunctionalInterface
public interface Caster {

    <T> T cast(Object value, Class<T> targetType);

}
