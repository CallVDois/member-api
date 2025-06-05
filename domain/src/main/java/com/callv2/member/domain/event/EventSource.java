package com.callv2.member.domain.event;

import java.util.Optional;

@FunctionalInterface
public interface EventSource {

    Optional<Event<?>> nextEvent();

}
