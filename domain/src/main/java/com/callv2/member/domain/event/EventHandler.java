package com.callv2.member.domain.event;

import java.io.Serializable;

@FunctionalInterface
public interface EventHandler<D extends Serializable> {

    void handle(Event<D> event);

}
