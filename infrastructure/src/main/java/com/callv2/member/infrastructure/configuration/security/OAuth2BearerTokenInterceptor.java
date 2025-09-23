package com.callv2.member.infrastructure.configuration.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class OAuth2BearerTokenInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        request.getHeaders()
                .setBearerAuth(Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                        .map(Authentication::getCredentials)
                        .map(it -> (Jwt) it)
                        .map(Jwt::getTokenValue)
                        .orElse(""));

        return execution.execute(request, body);

    }

}
