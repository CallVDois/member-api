package com.callv2.member.infrastructure.keycloak.model;

import java.util.Objects;

public class AllOfCredentialRepresentationConfig extends MultivaluedHashMapStringString {

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

}
