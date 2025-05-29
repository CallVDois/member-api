package com.callv2.member.infrastructure.keycloak.model;

import java.util.List;
import java.util.Map;

public record UserRepresentation(
        String id,
        String username,
        String firstName,
        String lastName,
        String email,
        Boolean emailVerified,
        Map<String, List<String>> attributes,
        UserProfileMetadata userProfileMetadata,
        String self,
        String origin,
        Long createdTimestamp,
        Boolean enabled,
        Boolean totp,
        String federationLink,
        String serviceAccountClientId,
        List<CredentialRepresentation> credentials,
        List<String> disableableCredentialTypes,
        List<String> requiredActions,
        List<FederatedIdentityRepresentation> federatedIdentities,
        List<String> realmRoles,
        Map<String, List<String>> clientRoles,
        List<UserConsentRepresentation> clientConsents,
        Integer notBefore,
        Map<String, List<String>> applicationRoles,
        List<SocialLinkRepresentation> socialLinks,
        List<String> groups,
        Map<String, Boolean> access) {

}
