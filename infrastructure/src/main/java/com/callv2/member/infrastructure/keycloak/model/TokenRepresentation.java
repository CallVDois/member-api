package com.callv2.member.infrastructure.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenRepresentation(
        @JsonProperty("access_token") String access_token,
        @JsonProperty("expires_in") Long expires_in,
        @JsonProperty("refresh_expires_in") Long refresh_expires_in,
        @JsonProperty("refresh_token") String refresh_token,
        @JsonProperty("token_type") String token_type,
        @JsonProperty("not-before-policy") Long not_before_policy,
        @JsonProperty("session_state") String session_state,
        String scope) {

}
