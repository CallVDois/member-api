package com.callv2.member.infrastructure.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.callv2.member.domain.pagination.Filter;
import com.callv2.member.domain.pagination.Page;
import com.callv2.member.domain.pagination.Pagination;
import com.callv2.member.infrastructure.api.controller.ApiError;
import com.callv2.member.infrastructure.member.model.GetMemberResponse;
import com.callv2.member.infrastructure.member.model.MemberListResponse;
import com.callv2.member.infrastructure.member.model.UpdateMemberSystemsRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

    @PutMapping("{id}/systems")
    @Operation(summary = "Update member available systems", description = "This method update a member available systems", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Member available systems is successfuly updated"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    ResponseEntity<Void> updateAvailableSystems(
            @PathVariable(value = "id", required = true) String id,
            @RequestBody UpdateMemberSystemsRequest request);

    @GetMapping("{id}")
    @Operation(summary = "Get member account", description = "This method get a member account", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member account found", content = @Content(schema = @Schema(implementation = GetMemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    ResponseEntity<GetMemberResponse> get(@PathVariable(value = "id", required = true) String id);

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "List members", description = "This method list files", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Files listed successfully", content = @Content(schema = @Schema(implementation = Page.class, subTypes = {
                    MemberListResponse.class }))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<Page<MemberListResponse>> list(
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "orderField", required = false, defaultValue = "createdAt") String orderField,
            @RequestParam(name = "orderDirection", required = false, defaultValue = "DESC") Pagination.Order.Direction orderDirection,
            @RequestParam(name = "filterOperator", required = false, defaultValue = "AND") Filter.Operator filterOperator,
            @RequestParam(name = "filters", required = false) List<String> filters);

}
