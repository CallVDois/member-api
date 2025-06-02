package com.callv2.member.domain.event;

import java.time.Instant;

public interface Event<D> {

    String id();

    String name();

    String source();

    Instant generatedAt();

    D data();

}
