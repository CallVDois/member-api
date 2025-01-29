package com.callv2.user.infrastructure.keycloak.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.callv2.user.infrastructure.keycloak.exception.BadRequestException;
import com.callv2.user.infrastructure.keycloak.exception.ForbiddenException;
import com.callv2.user.infrastructure.keycloak.exception.InternalServerError;
import com.callv2.user.infrastructure.keycloak.exception.UnauthorizedException;
import com.callv2.user.infrastructure.keycloak.exception.UserAlreadyExistsException;
import com.callv2.user.infrastructure.keycloak.model.Error;
import com.callv2.user.infrastructure.keycloak.model.ErrorRepresentation;
import com.callv2.user.infrastructure.keycloak.model.UserRepresentation;
import com.callv2.user.infrastructure.webclient.WebClientExceptionHandler;

import reactor.core.publisher.Mono;

public class KeycloakUserService {

    private final WebClient client;
    private final String realm;

    private final Pattern USER_ID_LOCATION_PATTERN = Pattern.compile("(?<=(.*\\/users\\/)).*");

    public KeycloakUserService(
            final WebClient client,
            final String realm) {
        this.client = client;
        this.realm = realm;
    }

    public String createUser(final UserRepresentation userRepresentation) {
        final ResponseEntity<Void> response = onStatus(client.post()
                .uri("/admin/realms/{realm}/users", realm)
                .bodyValue(userRepresentation)
                .retrieve())
                .toBodilessEntity()
                .block();

        final String userId = Optional
                .ofNullable(response.getHeaders().getLocation())
                .map(location -> USER_ID_LOCATION_PATTERN.matcher(location.toString()))
                .map(matcher -> matcher.find() ? matcher.group() : null)
                .orElseThrow(() -> InternalServerError.from("User creation failed"));

        return userId;
    }

    public void updateUser(final UserRepresentation userRepresentation, final String userId) {
        onStatus(client.put()
                .uri("/admin/realms/{realm}/users/{userId}", realm, userId)
                .bodyValue(userRepresentation)
                .retrieve())
                .toBodilessEntity()
                .block();
    }

    public void deleteUser(final String userId) {
        onStatus(client.delete()
                .uri("/admin/realms/{realm}/users/{userId}", realm, userId)
                .retrieve())
                .toBodilessEntity()
                .block();
    }

    private ResponseSpec onStatus(final ResponseSpec responseSpec) {
        return responseSpec
                .onStatus(BadRequestException.isSame(),
                        WebClientExceptionHandler.throwsException(ErrorRepresentation.class, BadRequestException::from))
                .onStatus(BadRequestException.isSame(),
                        WebClientExceptionHandler.throwsException(ErrorRepresentation.class, BadRequestException::from))
                .onStatus(UnauthorizedException.isSame(),
                        WebClientExceptionHandler.throwsException(Error.class, UnauthorizedException::from))
                .onStatus(ForbiddenException.isSame(),
                        WebClientExceptionHandler.throwsException(Error.class, ForbiddenException::from))
                .onStatus(UserAlreadyExistsException.isSame(),
                        WebClientExceptionHandler.throwsException(ErrorRepresentation.class,
                                UserAlreadyExistsException::from))
                .onStatus(HttpStatusCode::is5xxServerError,
                        res -> res.bodyToMono(String.class).flatMap(error -> Mono.error(new RuntimeException(error))));
    }

}
