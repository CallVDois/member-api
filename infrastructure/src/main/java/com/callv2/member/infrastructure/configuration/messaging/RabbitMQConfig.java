package com.callv2.member.infrastructure.configuration.messaging;

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

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitMQProducer<Event<MemberCreatedEvent.Data>> memberCreatedProducer(final RabbitTemplate rabbitTemplate) {
        return new RabbitMQProducer<>(
                "member.exchange",
                "member.created",
                rabbitTemplate);
    }

    @Bean
    RabbitMQProducer<Event<MemberUpdatedEvent.Data>> memberUpdatedProducer(final RabbitTemplate rabbitTemplate) {
        return new RabbitMQProducer<>(
                "member.exchange",
                "member.updated",
                rabbitTemplate);
    }

    @Configuration
    static class Admin {

        @Bean
        TopicExchange memberExchange() {
            return new TopicExchange("member.exchange");
        }

    }

}
