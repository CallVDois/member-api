package com.callv2.member.infrastructure.configuration.keycloak;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.callv2.member.infrastructure.external.keycloak.service.KeycloakUserService;

@Configuration
public class KeycloakUserServiceConfig {

    private final WebClient webClientClientCredentials;
    private final WebClient webClientToken;
    private final String realm;

    public KeycloakUserServiceConfig(
            @Qualifier("keycloakWebClientClientCredentials") final WebClient webClientClientCredentials,
            @Qualifier("keycloakWebClientToken") final WebClient webClientToken,
            @Value("${keycloak.realm}") final String realm) {
        this.webClientClientCredentials = webClientClientCredentials;
        this.webClientToken = webClientToken;
        this.realm = realm;
    }

    @Bean("keycloakUserServiceClientCredentials")
    KeycloakUserService keycloakUserServiceClientCredentials() {
        return new KeycloakUserService(webClientClientCredentials, realm);
    }

    @Bean("keycloakUserServiceToken")
    KeycloakUserService keycloakUserServiceToken() {
        return new KeycloakUserService(webClientToken, realm);
    }

}
