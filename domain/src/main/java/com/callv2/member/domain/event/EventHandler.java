package com.callv2.member.domain.event;

import java.io.Serializable;

public interface EventHandler<D extends Serializable> {

    String eventKey();

    void handle(Event<D> event);

}
