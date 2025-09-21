package com.callv2.member.infrastructure.member.event;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.callv2.member.domain.event.Event;
import com.callv2.member.domain.event.EventHandler;
import com.callv2.member.domain.member.event.MemberUpdatedEvent;
import com.callv2.member.infrastructure.messaging.producer.Producer;

@Component
public class MemberUpdatedEventMessagingHandler implements EventHandler<MemberUpdatedEvent.Data> {

    private static final String EVENT_KEY = MemberUpdatedEvent.eventKey();
    private final Producer<Event<MemberUpdatedEvent.Data>> producer;

    public MemberUpdatedEventMessagingHandler(final Producer<Event<MemberUpdatedEvent.Data>> producer) {
        this.producer = Objects.requireNonNull(producer);
    }

    @Override
    public String eventKey() {
        return EVENT_KEY;
    }

    @Override
    public void handle(final Event<MemberUpdatedEvent.Data> event) {
        producer.send(event);
    }

}
