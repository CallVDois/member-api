package com.callv2.member.infrastructure.configuration.keycloak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

import com.callv2.member.infrastructure.configuration.security.OAuth2BearerTokenInterceptor;
import com.callv2.member.infrastructure.configuration.security.OAuth2ClientTokenInterceptor;

@Configuration
public class ClientConfig {

    private static final String CLIENT_REGISTRATION_ID = "keycloak";

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public ClientConfig(final OAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
    }

    @Bean("keycloakRestClientClientCredentials")
    RestClient keycloakRestClientClientCredentials(
            @Value("${keycloak.host}") final String host) {

        final var oAuth2ClientTokenInterceptor = new OAuth2ClientTokenInterceptor(
                authorizedClientManager,
                CLIENT_REGISTRATION_ID);

        return RestClient
                .builder()
                .baseUrl(host)
                .requestInterceptor(oAuth2ClientTokenInterceptor)
                .build();
    }

    @Bean("keycloakRestClientBearerToken")
    RestClient keycloakRestClientBearerToken(
            OAuth2BearerTokenInterceptor oauth2BearerTokenInterceptor,
            @Value("${keycloak.host}") final String host) {
        return RestClient
                .builder()
                .baseUrl(host)
                .requestInterceptor(oauth2BearerTokenInterceptor)
                .build();
    }

}
