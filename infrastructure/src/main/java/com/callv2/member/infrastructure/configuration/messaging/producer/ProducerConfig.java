package com.callv2.member.infrastructure.configuration.messaging.producer;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.member.domain.event.Event;
import com.callv2.member.domain.member.event.MemberCreatedEvent;
import com.callv2.member.infrastructure.configuration.properties.messaging.RabbitMQProducerProperties;
import com.callv2.member.infrastructure.messaging.producer.Producer;
import com.callv2.member.infrastructure.messaging.producer.rabbitmq.RabbitMQProducer;

@Configuration
public class ProducerConfig {

    private final RabbitTemplate rabbitTemplate;
    private final Map<String, RabbitMQProducerProperties> rabbitMQPropertiesProducerProperties;

    public ProducerConfig(
            final RabbitTemplate rabbitTemplate,
            final Map<String, RabbitMQProducerProperties> producerProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQPropertiesProducerProperties = producerProperties;
    }

    @Bean
    Producer<Event<MemberCreatedEvent.Data>> memberCreatedEventRabbitMQProducer() {
        final var properties = this.rabbitMQPropertiesProducerProperties.get("member-created");
        return new RabbitMQProducer<>(
                properties.getExchange(),
                properties.getRoutingKey(),
                this.rabbitTemplate);
    }

}
