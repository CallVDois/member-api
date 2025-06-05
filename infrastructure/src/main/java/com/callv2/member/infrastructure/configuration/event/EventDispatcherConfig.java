package com.callv2.member.infrastructure.configuration.event;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.member.domain.event.EventDispatcher;
import com.callv2.member.domain.event.EventHandler;

@Configuration
public class EventDispatcherConfig {

    @Bean
    EventDispatcher eventDispatcher(final List<EventHandler<?>> eventHandlers) {
        final var dispatcher = new EventDispatcher();
        eventHandlers.forEach(handler -> dispatcher.register(handler.eventName(), handler));
        return dispatcher;
    }

}
