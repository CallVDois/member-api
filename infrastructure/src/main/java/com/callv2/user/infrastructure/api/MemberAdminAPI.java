package com.callv2.user.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.callv2.user.infrastructure.member.model.GetMemberResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Members Admin")
@RequestMapping("admin/members")
public interface MemberAdminAPI {

    @PutMapping("{id}/active")
    @Operation(summary = "Active/deactive member account", description = "This method active/deactive a member account", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Member account active/deactive successfuly"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    ResponseEntity<Void> toggleActive(
            @PathVariable(value = "id", required = true) String id,
            @RequestParam(value = "active", defaultValue = "true") boolean active);

    @GetMapping("{id}")
    @Operation(summary = "Get member account", description = "This method get a member account", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member account found", content = @Content(schema = @Schema(implementation = GetMemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    ResponseEntity<GetMemberResponse> get(@PathVariable(value = "id", required = true) String id);

}
