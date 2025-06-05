package com.callv2.member.infrastructure.external.keycloak.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.callv2.member.infrastructure.exception.BadRequestException;
import com.callv2.member.infrastructure.exception.ConflictException;
import com.callv2.member.infrastructure.exception.ForbiddenException;
import com.callv2.member.infrastructure.exception.InternalServerError;
import com.callv2.member.infrastructure.exception.NotFoundException;
import com.callv2.member.infrastructure.exception.UnauthorizedException;
import com.callv2.member.infrastructure.external.keycloak.model.Error;
import com.callv2.member.infrastructure.external.keycloak.model.ErrorRepresentation;
import com.callv2.member.infrastructure.external.keycloak.model.GroupRepresentation;
import com.callv2.member.infrastructure.external.keycloak.model.UserRepresentation;
import com.callv2.member.infrastructure.webclient.WebClientExceptionHandler;

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

    public void updateUser(final String userId, final UserRepresentation userRepresentation) {
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

    public List<GroupRepresentation> getGroups(final String userId) {
        return onStatus(client.get()
                .uri("/admin/realms/{realm}/users/{userId}/groups", realm, userId)
                .retrieve())
                .bodyToMono(new ParameterizedTypeReference<List<GroupRepresentation>>() {
                })
                .block();
    }

    public void addGroup(final String userId, final String groupId) {
        onStatus(client.put()
                .uri("/admin/realms/{realm}/users/{userId}/groups/{groupId}", realm, userId, groupId)
                .retrieve())
                .toBodilessEntity()
                .block();
    }

    public void deleteGroup(final String userId, final String groupId) {
        onStatus(client.delete()
                .uri("/admin/realms/{realm}/users/{userId}/groups/{groupId}", realm, userId, groupId)
                .retrieve())
                .toBodilessEntity()
                .block();
    }

    private ResponseSpec onStatus(final ResponseSpec responseSpec) {
        return responseSpec

                .onStatus(HttpStatus.BAD_REQUEST::isSameCodeAs,
                        WebClientExceptionHandler
                                .throwsException(
                                        ErrorRepresentation.class,
                                        error -> BadRequestException.from(error.errorMessage())))

                .onStatus(HttpStatus.UNAUTHORIZED::isSameCodeAs,
                        WebClientExceptionHandler.throwsException(
                                Error.class, error -> UnauthorizedException.from(error.error())))

                .onStatus(HttpStatus.FORBIDDEN::isSameCodeAs,
                        WebClientExceptionHandler.throwsException(Error.class,
                                error -> ForbiddenException.from(error.error())))

                .onStatus(HttpStatus.CONFLICT::isSameCodeAs,
                        WebClientExceptionHandler.throwsException(ErrorRepresentation.class,
                                error -> ConflictException.from(error.errorMessage())))

                .onStatus(HttpStatus.NOT_FOUND::isSameCodeAs,
                        WebClientExceptionHandler.throwsException(ErrorRepresentation.class,
                                error -> NotFoundException.from(error.errorMessage())))

                .onStatus(HttpStatusCode::is5xxServerError,
                        WebClientExceptionHandler.throwsException(String.class, InternalServerError::from));
    }

}
