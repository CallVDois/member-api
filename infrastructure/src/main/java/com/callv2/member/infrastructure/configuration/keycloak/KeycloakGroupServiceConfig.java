package com.callv2.member.infrastructure.configuration.keycloak;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.callv2.member.infrastructure.external.keycloak.service.KeycloakGroupService;

@Configuration
public class KeycloakGroupServiceConfig {

    private final WebClient webClientClientCredentials;
    private final String realm;

    public KeycloakGroupServiceConfig(
            @Qualifier("keycloakWebClientClientCredentials") final WebClient webClientClientCredentials,
            @Value("${keycloak.realm}") final String realm) {
        this.webClientClientCredentials = webClientClientCredentials;
        this.realm = realm;
    }

    @Bean("keycloakGroupServiceClientCredentials")
    KeycloakGroupService keycloakGroupServiceClientCredentials() {
        return new KeycloakGroupService(webClientClientCredentials, realm);
    }

}
