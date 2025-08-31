package com.callv2.member.infrastructure.configuration.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.member.domain.event.Event;
import com.callv2.member.domain.member.event.MemberCreatedEvent;
import com.callv2.member.domain.member.event.MemberUpdatedEvent;
import com.callv2.member.infrastructure.messaging.producer.rabbitmq.RabbitMQProducer;

@Configuration
public class RabbitMQConfig {

    private static final String EVENT_HUB_EXCHANGE_NAME = "eventhub.exchange";
    private static final String EVENT_HUB_EXCHANGE_ROUTING_KEY = "member.#.event";

    private static final String MEMBER_EXCHANGE_NAME = "member.exchange";
    private static final String MEMBER_DLX_EXCHANGE_NAME = "member.dlx.exchange";

    private static final String MEMBER_CREATED_ROUTING_KEY = "member.member.created.event";

    private static final String MEMBER_UPDATED_ROUTING_KEY = "member.member.updated.event";

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitMQProducer<Event<MemberCreatedEvent.Data>> memberCreatedProducer(final RabbitTemplate rabbitTemplate) {
        return new RabbitMQProducer<>(
                MEMBER_EXCHANGE_NAME,
                MEMBER_CREATED_ROUTING_KEY,
                rabbitTemplate);
    }

    @Bean
    RabbitMQProducer<Event<MemberUpdatedEvent.Data>> memberUpdatedProducer(final RabbitTemplate rabbitTemplate) {
        return new RabbitMQProducer<>(
                MEMBER_EXCHANGE_NAME,
                MEMBER_UPDATED_ROUTING_KEY,
                rabbitTemplate);
    }

    @Configuration
    static class Admin {

        private final TopicExchange eventHubExchange = new TopicExchange(EVENT_HUB_EXCHANGE_NAME);

        private final TopicExchange memberExchange = new TopicExchange(MEMBER_EXCHANGE_NAME);
        private final TopicExchange memberDlxExchange = new TopicExchange(MEMBER_DLX_EXCHANGE_NAME);

        public final Binding memberEventsBinding = BindingBuilder
                .bind(eventHubExchange)
                .to(memberExchange)
                .with(EVENT_HUB_EXCHANGE_ROUTING_KEY);

        @Bean
        TopicExchange eventHubExchange() {
            return eventHubExchange;
        }

        @Bean
        TopicExchange memberExchange() {
            return memberExchange;
        }

        @Bean
        TopicExchange memberDlxExchange() {
            return memberDlxExchange;
        }

        @Bean
        Binding memberEventsBinding() {
            return memberEventsBinding;
        }

    }

}
