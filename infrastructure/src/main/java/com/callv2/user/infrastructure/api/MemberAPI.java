package com.callv2.user.infrastructure.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.callv2.user.infrastructure.api.controller.ApiError;
import com.callv2.user.infrastructure.member.model.CreateMemberRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Members")
@RequestMapping("members")
public interface MemberAPI {

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Create member account", description = "This method create a new member account")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created successfuly"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<Void> create(@RequestBody CreateMemberRequest request);

    @PutMapping("{id}/active")
    @Operation(summary = "Active/deactive member account", description = "This method active/deactive a member account", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Member account active/deactive successfuly"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    ResponseEntity<Void> toggleActive(
            @PathVariable(value = "id", required = true) String id,
            @RequestParam(value = "active", defaultValue = "true") boolean active);

}