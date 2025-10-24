package com.callv2.member.infrastructure.external.keycloak.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

import com.callv2.member.infrastructure.exception.BadRequestException;
import com.callv2.member.infrastructure.exception.ConflictException;
import com.callv2.member.infrastructure.exception.ForbiddenException;
import com.callv2.member.infrastructure.exception.InternalServerError;
import com.callv2.member.infrastructure.exception.NotFoundException;
import com.callv2.member.infrastructure.exception.UnauthorizedException;
import com.callv2.member.infrastructure.external.keycloak.model.Error;
import com.callv2.member.infrastructure.external.keycloak.model.ErrorRepresentation;
import com.callv2.member.infrastructure.external.keycloak.model.GroupRepresentation;
import com.callv2.member.infrastructure.restclient.RestClientExceptionHandler;

public class KeycloakGroupService {

    private final RestClient client;
    private final String realm;

    public KeycloakGroupService(
            final RestClient client,
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

        while (!pathBuilder.toString().equals(path)) {
            group = getGroupChildrens(group.id())
                    .stream()
                    .filter(g -> path.startsWith(g.path()))
                    .findAny()
                    .orElseThrow(() -> NotFoundException.from("Group not found for path: " + path));
            pathBuilder.append("/" + group.name());
        }

        return group;
    }

    public List<GroupRepresentation> getGroups() {
        return onStatus(client.get()
                .uri("/admin/realms/{realm}/groups", realm)
                .retrieve())
                .body(new ParameterizedTypeReference<List<GroupRepresentation>>() {
                });
    }

    public List<GroupRepresentation> getGroupChildrens(final String groupId) {
        return onStatus(client.get()
                .uri("/admin/realms/{realm}/groups/{groupId}/children", realm, groupId)
                .retrieve())
                .body(new ParameterizedTypeReference<List<GroupRepresentation>>() {
                });
    }

    private ResponseSpec onStatus(final ResponseSpec responseSpec) {
        return responseSpec

                .onStatus(
                        HttpStatus.BAD_REQUEST::isSameCodeAs,
                        (request, response) -> RestClientExceptionHandler.throwsException(
                                ErrorRepresentation.class,
                                request,
                                response,
                                error -> BadRequestException.from(error.errorMessage())))

                .onStatus(
                        HttpStatus.UNAUTHORIZED::isSameCodeAs,
                        (request, response) -> RestClientExceptionHandler.throwsException(
                                Error.class,
                                request,
                                response,
                                error -> UnauthorizedException.from(error.error())))

                .onStatus(
                        HttpStatus.FORBIDDEN::isSameCodeAs,
                        (request, response) -> RestClientExceptionHandler.throwsException(
                                Error.class,
                                request,
                                response,
                                error -> ForbiddenException.from(error.error())))

                .onStatus(
                        HttpStatus.CONFLICT::isSameCodeAs,
                        (request, response) -> RestClientExceptionHandler.throwsException(
                                ErrorRepresentation.class,
                                request,
                                response,
                                error -> ConflictException.from(error.errorMessage())))

                .onStatus(
                        HttpStatus.NOT_FOUND::isSameCodeAs,
                        (request, response) -> RestClientExceptionHandler.throwsException(
                                ErrorRepresentation.class,
                                request,
                                response,
                                error -> NotFoundException.from(error.errorMessage())))

                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        (request, response) -> RestClientExceptionHandler.throwsException(
                                String.class,
                                request,
                                response,
                                InternalServerError::from));

    }

}
