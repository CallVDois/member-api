package com.callv2.user.infrastructure.keycloak.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.callv2.user.domain.member.PreMember;
import com.callv2.user.infrastructure.keycloak.model.CredentialRepresentation;
import com.callv2.user.infrastructure.keycloak.model.UserRepresentation;

@Mapper(componentModel = "spring")
public interface KeycloakUserMapper {

    @Mapping(target = "type", constant = "password")
    @Mapping(target = "temporary", constant = "false")
    @Mapping(target = "value", source = "password.value")
    CredentialRepresentation toCredentialRepresentation(PreMember preMember);

    @Mapping(target = "username", source = "username.value")
    @Mapping(target = "email", source = "email.value")
    @Mapping(target = "credentials", source = "preMember")
    UserRepresentation toUserRepresentation(PreMember preMember);

    default List<CredentialRepresentation> toCredentialRepresentationList(
            CredentialRepresentation credentialRepresentation) {
        return credentialRepresentation != null ? List.of(credentialRepresentation) : List.of();
    }

}
