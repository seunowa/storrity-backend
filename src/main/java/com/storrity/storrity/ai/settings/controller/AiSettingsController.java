/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.controller;

import com.storrity.storrity.ai.settings.dto.AiProvider;
import com.storrity.storrity.ai.settings.dto.ProviderInfo;
import com.storrity.storrity.ai.settings.service.AiConfigService;
import com.storrity.storrity.util.exception.AuthorizationError;
import com.storrity.storrity.util.exception.ServerError;
import com.storrity.storrity.util.exception.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/ai/settings")
@Tag(name = "AI Settings", description = "Operations related to AI provider configuration and settings")
@RequiredArgsConstructor
public class AiSettingsController {

    private final AiConfigService aiConfigService;

    // ---------- Set / Update Provider Key ----------
    @Operation(
            operationId = "saveAiProvider",
            summary = "Create or Update provider details - api key",
            description = "Update api model provider details",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Provider key saved successfully",
            content = @Content(schema = @Schema(implementation = String.class))
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
    @PutMapping("/providers/{provider}")
    public ResponseEntity<String> saveProvider(
            @PathVariable String provider,
            @RequestBody @Valid SaveProviderRequest request) {

        aiConfigService.saveProviderKey(
                provider,
                request.apiKey(),
                request.baseUrl(),
                request.defaultModel()
        );
        return ResponseEntity.ok("Provider key saved");
    }

    // ---------- Select active provider + model ----------
    @Operation(
            operationId = "setActiveAiProvider",
            summary = "Set active provider",
            description = "Set the api model provider to use for AI requests",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Active model updated successfully",
            content = @Content(schema = @Schema(implementation = String.class))
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
    @PutMapping("/active")
    public ResponseEntity<String> setActive(@RequestBody @Valid SetActiveRequest request) {
        aiConfigService.setActiveProvider(request.provider(), request.model());
        return ResponseEntity.ok("Active model updated");
    }

    // ---------- Status ----------
    @Operation(
            operationId = "getAiSettingsStatus",
            summary = "Get the api provider's status",
            description = "Get the api provider's status",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "AI settings status retrieved successfully",
            content = @Content(schema = @Schema(implementation = AiStatusResponse.class))
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
    @GetMapping("/status")
    public AiStatusResponse getStatus() {
        var active = aiConfigService.getActiveConfig();
        var providers = aiConfigService.getAllEnabledProviders().stream()
                .map(p -> p.getProvider())
                .toList();

        return new AiStatusResponse(
                active.getActiveProvider(),
                aiConfigService.getActiveModel(),
                providers
        );
    }
    
    @Operation(
            operationId = "listSupportedProviders",
            summary = "List supported AI providers with recommended models",
            description = "List supported AI providers with recommended models",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Supported AI providers retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = ProviderInfo.class))
            )
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
    @GetMapping("/providers")
    public List<ProviderInfo> listSupportedProviders() {
        return Arrays.stream(AiProvider.values())
                .map(p -> new ProviderInfo(
                        p.name(),
                        p.getDisplayName(),
                        p.getDefaultBaseUrl(),
                        p.getRecommendedModel(),
                        p.getDescription()
                ))
                .toList();
    }

    public record SaveProviderRequest(String apiKey, String baseUrl, String defaultModel) {}
    public record SetActiveRequest(String provider, String model) {}
    public record AiStatusResponse(String activeProvider, String activeModel, List<String> configuredProviders) {}
}
