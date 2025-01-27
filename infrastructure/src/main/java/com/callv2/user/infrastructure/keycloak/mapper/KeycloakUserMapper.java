package com.callv2.user.infrastructure.keycloak.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.callv2.user.domain.member.PreMember;
import com.callv2.user.infrastructure.keycloak.model.CredentialRepresentation;
import com.callv2.user.infrastructure.keycloak.model.UserRepresentation;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface KeycloakUserMapper {

    @Mapping(target = "username", source = "username.value")
    @Mapping(target = "email", source = "email.value")
    @Mapping(target = "credentials", source = "preMember")
    @Mapping(target = "groups", expression = "java(defaulMemberstGroups())")
    UserRepresentation toUserRepresentation(PreMember preMember);

    @Mapping(target = "type", constant = "password")
    @Mapping(target = "temporary", constant = "false")
    @Mapping(target = "value", source = "password.value")
    CredentialRepresentation toCredentialRepresentation(PreMember preMember);

    default List<CredentialRepresentation> toCredentialRepresentationList(
            final CredentialRepresentation credentialRepresentation) {
        return credentialRepresentation != null ? List.of(credentialRepresentation) : List.of();
    }

    @Named("defaulMemberstGroups")
    default List<String> defaulMemberstGroups() {
        return List.of("membros");
    }

}
