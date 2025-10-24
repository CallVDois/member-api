package com.callv2.member.infrastructure.configuration.keycloak;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.callv2.member.infrastructure.external.keycloak.service.KeycloakGroupService;

@Configuration
public class KeycloakGroupServiceConfig {

    private final RestClient restClientClientCredentials;
    private final String realm;

    public KeycloakGroupServiceConfig(
            @Qualifier("keycloakRestClientClientCredentials") final RestClient restClientClientCredentials,
            @Value("${keycloak.realm}") final String realm) {
        this.restClientClientCredentials = restClientClientCredentials;
        this.realm = realm;
    }

    @Bean("keycloakGroupServiceClientCredentials")
    KeycloakGroupService keycloakGroupServiceClientCredentials() {
        return new KeycloakGroupService(restClientClientCredentials, realm);
    }

}
