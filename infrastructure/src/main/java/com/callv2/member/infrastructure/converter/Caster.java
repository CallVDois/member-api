package com.callv2.member.infrastructure.converter;

@FunctionalInterface
public interface Caster {

    <T> T cast(Object value, Class<T> targetType);

}
