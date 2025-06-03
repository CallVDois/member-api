package com.callv2.member.infrastructure.member.event;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.callv2.member.domain.event.Event;
import com.callv2.member.domain.event.EventHandler;
import com.callv2.member.domain.member.MemberCreatedEvent;
import com.callv2.member.infrastructure.messaging.producer.Producer;

@Component
public class MemberCreatedEventMessagingHandler implements EventHandler<MemberCreatedEvent.Data> {

    private final Producer<Event<MemberCreatedEvent.Data>> producer;

    public MemberCreatedEventMessagingHandler(final Producer<Event<MemberCreatedEvent.Data>> producer) {
        this.producer = Objects.requireNonNull(producer);
    }

    @Override
    public void handle(final Event<MemberCreatedEvent.Data> event) {
        producer.send(event);
    }

}
