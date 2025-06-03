package com.callv2.member.domain.event;

import java.io.Serializable;
import java.time.Instant;

public interface Event<D extends Serializable> extends Serializable {

    String id();

    String name();

    String source();

    Instant occurredAt();

    D data();

}
