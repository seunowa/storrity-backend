/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.controller;

import com.storrity.storrity.ai.reports.dto.BusinessContextCreationDto;
import com.storrity.storrity.ai.reports.dto.BusinessContextDto;
import com.storrity.storrity.ai.settings.service.BusinessContextService;
import com.storrity.storrity.util.exception.ApiError;
import com.storrity.storrity.util.exception.AuthorizationError;
import com.storrity.storrity.util.exception.ServerError;
import com.storrity.storrity.util.exception.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/ai/business_context")
@RequiredArgsConstructor
@Tag(name = "Business Context", description = "Operations related to AI business context management")
public class BusinessContextController {

    private final BusinessContextService service;

    @Operation(
            operationId = "getGlobalBusinessContext",
            description = "Get global AI business context",
            summary = "Get global business context",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Global business context retrieved successfully",
            content = @Content(schema = @Schema(implementation = BusinessContextDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Global context not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping
    public BusinessContextDto getGlobal() {
        return service.getGlobal();
    }

    @Operation(
            operationId = "getStoreBusinessContext",
            description = "Get AI business context for a specific store by store ID",
            summary = "Get business context for store",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Store business context retrieved successfully",
            content = @Content(schema = @Schema(implementation = BusinessContextDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business context for store not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("/store/{storeId}")
    public BusinessContextDto getForStore(@PathVariable UUID storeId) {
        return service.getForStore(storeId);
    }

    @Operation(
            operationId = "saveGlobalBusinessContext",
            description = "Create or update global AI business context",
            summary = "Save global business context",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Global business context saved successfully",
            content = @Content(schema = @Schema(implementation = BusinessContextDto.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "403", 
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PutMapping
    public BusinessContextDto saveGlobal(@RequestBody @Valid BusinessContextCreationDto request) {
        
        return service.saveGlobalCtx(request);
    }

    @Operation(
            operationId = "saveStoreBusinessContext",
            description = "Create or update AI business context for a specific store",
            summary = "Save store business context",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Store business context saved successfully",
            content = @Content(schema = @Schema(implementation = BusinessContextDto.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "403", 
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PutMapping("/store/{storeId}")
    public BusinessContextDto saveForStore(@PathVariable UUID storeId,
                                        @RequestBody @Valid BusinessContextCreationDto request) {
        return service.saveStoreCtx(storeId, request);
    }
}
