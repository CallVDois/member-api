package com.callv2.member.infrastructure.member.model;

import java.util.Set;

import com.callv2.member.domain.member.valueobject.System;

public record UpdateMemberSystemsRequest(Set<System> systems) {

}
