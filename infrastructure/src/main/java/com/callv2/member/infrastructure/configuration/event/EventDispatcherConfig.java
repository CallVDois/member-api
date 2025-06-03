package com.callv2.member.infrastructure.configuration.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.member.domain.event.EventDispatcher;
import com.callv2.member.infrastructure.member.event.MemberCreatedEventMessagingHandler;

@Configuration
public class EventDispatcherConfig {

    @Bean
    EventDispatcher eventDispatcher(final MemberCreatedEventMessagingHandler memberCreatedEventMessagingHandler) {
        final var dispatcher = new EventDispatcher();
        dispatcher.register("member.created", memberCreatedEventMessagingHandler);
        return dispatcher;
    }

}
