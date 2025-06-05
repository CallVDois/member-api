package com.callv2.member.infrastructure.configuration.gateway;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.member.domain.member.gateway.MemberGateway;
import com.callv2.member.infrastructure.external.keycloak.mapper.KeycloakGroupMapper;
import com.callv2.member.infrastructure.external.keycloak.mapper.KeycloakUserMapper;
import com.callv2.member.infrastructure.external.keycloak.service.KeycloakGroupService;
import com.callv2.member.infrastructure.external.keycloak.service.KeycloakUserService;
import com.callv2.member.infrastructure.filter.FilterService;
import com.callv2.member.infrastructure.member.DefaultMemberGateway;
import com.callv2.member.infrastructure.member.persistence.MemberJpaRepository;

@Configuration
public class MemberGatewayConfig {

    @Bean
    MemberGateway memberGateway(
            final FilterService filterService,
            final MemberJpaRepository memberJpaRepository,
            final KeycloakUserMapper keycloakUserMapper,
            @Qualifier("keycloakUserServiceClientCredentials") final KeycloakUserService clientKeycloakUserService,
            @Qualifier("keycloakUserServiceToken") final KeycloakUserService tokenKeycloakUserService,
            final KeycloakGroupMapper keycloakGroupMapper,
            final KeycloakGroupService keycloakGroupService) {
        return new DefaultMemberGateway(
                filterService,
                memberJpaRepository,
                keycloakUserMapper,
                clientKeycloakUserService,
                tokenKeycloakUserService,
                keycloakGroupMapper,
                keycloakGroupService);
    }

}
