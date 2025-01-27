package com.callv2.user.infrastructure.configuration.keycloak;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.callv2.user.infrastructure.keycloak.service.KeycloakUserService;

@Configuration
public class ClientConfig {

    @Bean
    KeycloakUserService userService(@Qualifier("keycloakWebClient") final WebClient webClient) {
        return new KeycloakUserService(webClient);
    }

    @Bean(name = "keycloakWebClient")
    WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {

        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                authorizedClientManager);
        oauth2Client.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder()
                .baseUrl("http://localhost:8090")
                .apply(oauth2Client.oauth2Configuration())
                .build();
    }

}
