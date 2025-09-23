package com.callv2.member.infrastructure.configuration.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

public class OAuth2ClientTokenInterceptor implements ClientHttpRequestInterceptor {

    private static final String DEFAULT_PRINCIPAL = "member-api";

    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final String clientRegistrationId;

    public OAuth2ClientTokenInterceptor(
            OAuth2AuthorizedClientManager authorizedClientManager,
            String clientRegistrationId) {
        this.authorizedClientManager = authorizedClientManager;
        this.clientRegistrationId = clientRegistrationId;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        final String principal = Optional
                .ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElse(DEFAULT_PRINCIPAL);

        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(clientRegistrationId)
                .principal(principal)
                .build();

        Optional.ofNullable(authorizedClientManager.authorize(authorizeRequest))
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(token -> token.getTokenValue())
                .ifPresent(tokenValue -> request.getHeaders().setBearerAuth(tokenValue));

        return execution.execute(request, body);
    }

}
