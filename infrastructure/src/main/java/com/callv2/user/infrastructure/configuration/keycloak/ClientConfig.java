package com.callv2.user.infrastructure.configuration.keycloak;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.callv2.user.infrastructure.keycloak.service.UserService;

@Configuration
public class ClientConfig {

    @Bean(name = "keycloakWebClient")
    WebClient webClient() {
        return WebClient
                .create("http://localhost:8090");
    }

    @Bean
    UserService userService(@Qualifier("keycloakWebClient") final WebClient webClient) {
        return new UserService(webClient);
    }

}
