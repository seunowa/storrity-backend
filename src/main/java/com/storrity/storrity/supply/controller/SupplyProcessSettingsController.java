/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.controller;

import com.storrity.storrity.supply.entity.SupplyProcess;
import com.storrity.storrity.supply.entity.SupplyProcessTemplate;
import com.storrity.storrity.supply.service.SupplyProcessSettingsService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/v1/supply/settings")
@Tag(
    name = "Supply Process Settings",
    description = "Operations related to system-wide supply process settings"
)
public class SupplyProcessSettingsController {

    private final SupplyProcessSettingsService supplyProcessSettingsService;

    @Autowired
    public SupplyProcessSettingsController(
            SupplyProcessSettingsService supplyProcessSettingsService) {

        this.supplyProcessSettingsService =
                supplyProcessSettingsService;
    }

    @Operation(
        operationId = "getSupplyProcessSettings",
        description = "Get the current system-wide supply process settings",
        summary = "Get supply process settings",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Supply process settings retrieved successfully",
            content = @Content(
                schema = @Schema(
                    implementation = SupplyProcess.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Supply process settings have not been configured"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(
                    implementation = AuthorizationError.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(
                    implementation = ServerError.class
                )
            )
        )
    })
    @GetMapping
    public ResponseEntity<SupplyProcess> getSupplyProcessSettings() {

        SupplyProcess supplyProcess =
                supplyProcessSettingsService
                        .getSupplyProcessSettings();

        if (supplyProcess == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(supplyProcess);
    }

    @Operation(
        operationId = "updateSupplyProcessSettings",
        description = "Replace the existing system-wide supply process settings with the supplied configuration",
        summary = "Update supply process settings",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Supply process settings updated successfully",
            content = @Content(
                schema = @Schema(
                    implementation = SupplyProcess.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(
                schema = @Schema(
                    implementation = ValidationError.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(
                    implementation = AuthorizationError.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(
                    implementation = ServerError.class
                )
            )
        )
    })
    @PutMapping
    public SupplyProcess updateSupplyProcessSettings(
            @RequestBody SupplyProcess supplyProcess) {

        return supplyProcessSettingsService
                .updateSupplyProcessSettings(supplyProcess);
    }

    @Operation(
        operationId = "getSupplyProcessTemplates",
        description = "Get the available predefined supply process templates",
        summary = "Get supply process templates",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Supply process templates retrieved successfully",
            content = @Content(
                schema = @Schema(
                    implementation = SupplyProcessTemplate.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(
                    implementation = AuthorizationError.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(
                    implementation = ServerError.class
                )
            )
        )
    })
    @GetMapping("/templates")
    public SupplyProcessTemplate getTemplates() {

        return supplyProcessSettingsService.getTemplates();
    }
}
