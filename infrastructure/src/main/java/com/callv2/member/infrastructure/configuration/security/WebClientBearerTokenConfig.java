package com.callv2.member.infrastructure.configuration.security;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientBearerTokenConfig {

    @Bean("oauth2BearerTokenFilter")
    Consumer<WebClient.Builder> oauth2BearerTokenFilter() {
        return builder -> builder.filter((request, next) -> {
            return next.exchange(ClientRequest.from(request)
                    .header("Authorization",
                            "Bearer " + Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                                    .map(Authentication::getCredentials)
                                    .map(it -> (Jwt) it)
                                    .map(Jwt::getTokenValue)
                                    .orElse(""))
                    .build());
        });
    }

}
