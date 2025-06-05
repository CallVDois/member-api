package com.callv2.member.infrastructure.external.keycloak.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;

import com.callv2.member.domain.member.valueobject.System;

@Mapper
public interface KeycloakGroupMapper {

    @ValueMapping(target = "/callv2/drive/member", source = "DRIVE")
    @ValueMapping(target = "/callv2/member/member", source = "MEMBER")
    String toGroupPath(System system);

    Set<String> toGroupPaths(Set<System> systems);

}