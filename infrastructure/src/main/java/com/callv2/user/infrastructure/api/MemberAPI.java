package com.callv2.user.infrastructure.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.callv2.user.domain.member.QuotaUnit;
import com.callv2.user.domain.pagination.Pagination;
import com.callv2.user.domain.pagination.SearchQuery;
import com.callv2.user.infrastructure.api.controller.ApiError;
import com.callv2.user.infrastructure.member.model.CreateMemberRequest;
import com.callv2.user.infrastructure.member.model.QuotaRequestListResponse;

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

    @PostMapping("quotas/requests/{amount}")
    @Operation(summary = "Request drive quota", description = "This method request a drive ammount quota", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Requested successfuly"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    ResponseEntity<Void> requestQuota(
            @PathVariable(value = "amount", required = true) long amount,
            @RequestParam(value = "unit", defaultValue = "GIGABYTE") QuotaUnit unit);

    @PostMapping("{id}/quotas/requests/approve")
    @Operation(summary = "Approve drive quota request", description = "This method approve a drive ammount quota request", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Approved successfuly"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    ResponseEntity<Void> approveQuotaRequest(
            @PathVariable(value = "id", required = true) String id,
            @RequestParam(value = "approved", defaultValue = "true") boolean approved);

    @GetMapping(value = "quotas/requests", produces = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "List quotas requests", description = "This method list quotas requests")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quotas requests listed successfully", content = @Content(schema = @Schema(implementation = Pagination.class, subTypes = {
                    QuotaRequestListResponse.class }))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<Pagination<QuotaRequestListResponse>> list(
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "orderField", required = false, defaultValue = "quotaRequestedAt") String orderField,
            @RequestParam(name = "orderDirection", required = false, defaultValue = "DESC") SearchQuery.Order.Direction orderDirection,
            @RequestParam(name = "filterMethod", required = false, defaultValue = "AND") SearchQuery.FilterMethod filterMethod,
            @RequestParam(name = "filters", required = false) List<String> filters);

}