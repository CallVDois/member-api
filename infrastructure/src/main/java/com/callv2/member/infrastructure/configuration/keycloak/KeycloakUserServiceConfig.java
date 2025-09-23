package com.callv2.member.infrastructure.configuration.keycloak;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.callv2.member.infrastructure.external.keycloak.service.KeycloakUserService;

@Configuration
public class KeycloakUserServiceConfig {

    private final RestClient restClientClientCredentials;
    private final RestClient restClientToken;
    private final String realm;

    public KeycloakUserServiceConfig(
            @Qualifier("keycloakRestClientClientCredentials") final RestClient restClientClientCredentials,
            @Qualifier("keycloakRestClientBearerToken") final RestClient restClientToken,
            @Value("${keycloak.realm}") final String realm) {
        this.restClientClientCredentials = restClientClientCredentials;
        this.restClientToken = restClientToken;
        this.realm = realm;
    }

    @Bean("keycloakUserServiceClientCredentials")
    KeycloakUserService keycloakUserServiceClientCredentials() {
        return new KeycloakUserService(restClientClientCredentials, realm);
    }

    @Bean("keycloakUserServiceToken")
    KeycloakUserService keycloakUserServiceToken() {
        return new KeycloakUserService(restClientToken, realm);
    }

}
