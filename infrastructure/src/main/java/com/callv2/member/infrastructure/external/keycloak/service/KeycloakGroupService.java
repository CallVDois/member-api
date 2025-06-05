package com.callv2.member.infrastructure.external.keycloak.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.callv2.member.infrastructure.webclient.WebClientExceptionHandler;

public class KeycloakGroupService {

    private final WebClient client;
    private final String realm;

    public KeycloakGroupService(
            final WebClient client,
            final String realm) {
        this.client = client;
        this.realm = realm;
    }

    public GroupRepresentation getGroupByPath(final String path) {

        GroupRepresentation group = getGroups()
                .stream()
                .filter(g -> path.startsWith(g.path()))
                .findAny()
                .orElseThrow(() -> NotFoundException.from("Group not found for path: " + path));

        final StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append("/" + group.name());

        do {
            group = getGroupChildrens(group.id())
                    .stream()
                    .filter(g -> path.startsWith(g.path()))
                    .findAny()
                    .orElseThrow(() -> NotFoundException.from("Group not found for path: " + path));
            pathBuilder.append("/" + group.name());
        } while (!pathBuilder.toString().equals(path));

        return group;
    }

    public List<GroupRepresentation> getGroups() {
        return onStatus(client.get()
                .uri("/admin/realms/{realm}/groups", realm)
                .retrieve())
                .bodyToMono(new ParameterizedTypeReference<List<GroupRepresentation>>() {
                })
                .block();
    }

    public List<GroupRepresentation> getGroupChildrens(final String groupId) {
        return onStatus(client.get()
                .uri("/admin/realms/{realm}/groups/{groupId}/children", realm, groupId)
                .retrieve())
                .bodyToMono(new ParameterizedTypeReference<List<GroupRepresentation>>() {
                })
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

                .onStatus(HttpStatusCode::is5xxServerError,
                        WebClientExceptionHandler.throwsException(String.class, InternalServerError::from));
    }

}
