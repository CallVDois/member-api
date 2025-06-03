package com.callv2.member.infrastructure.messaging.producer;

@FunctionalInterface
public interface Producer<T> {

    void send(T data);

}
