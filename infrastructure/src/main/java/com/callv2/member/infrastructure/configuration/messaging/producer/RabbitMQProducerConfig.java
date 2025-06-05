package com.callv2.member.infrastructure.configuration.messaging.producer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.member.infrastructure.configuration.properties.messaging.RabbitMQProducerProperties;

@Configuration
public class RabbitMQProducerConfig {

    @Bean
    @ConfigurationProperties(prefix = "messaging.rabbitmq.producers")
    Map<String, RabbitMQProducerProperties> producerProperties() {
        return new HashMap<>();
    }

}
