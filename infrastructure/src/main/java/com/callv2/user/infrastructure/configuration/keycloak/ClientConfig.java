package com.callv2.user.infrastructure.configuration.keycloak;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.callv2.user.infrastructure.keycloak.service.KeycloakUserService;

@Configuration
public class ClientConfig {

    @Bean
    KeycloakUserService userService(
            @Qualifier("keycloakWebClient") final WebClient webClient,
            @Value("${keycloak.realm}") final String realm) {
        return new KeycloakUserService(webClient, realm);
    }

    @Bean(name = "keycloakWebClient")
    WebClient webClient(
            @Qualifier("keycloakOauth2Client") final ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client,
            @Value("${keycloak.host}") final String host) {
        return WebClient.builder()
                .baseUrl(host)
                .apply(oauth2Client.oauth2Configuration())
                .build();
    }

}
