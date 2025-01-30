package com.callv2.user.infrastructure.configuration.gateway;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.user.domain.member.MemberGateway;
import com.callv2.user.infrastructure.keycloak.mapper.KeycloakUserMapper;
import com.callv2.user.infrastructure.keycloak.service.KeycloakUserService;
import com.callv2.user.infrastructure.member.DefaultMemberGateway;
import com.callv2.user.infrastructure.member.persistence.MemberJpaRepository;

@Configuration
public class MemberGatewayConfig {

    @Bean
    MemberGateway memberGateway(
            final MemberJpaRepository memberJpaRepository,
            final KeycloakUserMapper keycloakUserMapper,
            @Qualifier("keycloakUserServiceClientCredentials") final KeycloakUserService clientKeycloakUserService,
            @Qualifier("keycloakUserServiceToken") final KeycloakUserService tokenKeycloakUserService) {
        return new DefaultMemberGateway(
                memberJpaRepository,
                keycloakUserMapper,
                clientKeycloakUserService,
                tokenKeycloakUserService);
    }

}
