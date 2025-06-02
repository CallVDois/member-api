package com.callv2.member.domain.event;

public interface EventHandler<D> {

    String eventName();

    void handle(Event<D> event);

}
