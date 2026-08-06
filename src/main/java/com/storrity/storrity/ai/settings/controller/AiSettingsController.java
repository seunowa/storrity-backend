/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.controller;

import com.storrity.storrity.ai.settings.dto.AiProvider;
import com.storrity.storrity.ai.settings.dto.ProviderInfo;
import com.storrity.storrity.ai.settings.service.AiConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RestController
@RequestMapping("/api/v1/ai/settings")
@Tag(name = "AI Settings")
@RequiredArgsConstructor
public class AiSettingsController {

    private final AiConfigService aiConfigService;

    // ---------- Set / Update Provider Key ----------
    @Operation(
            summary = "Create or Update provider details - api key",
            description = "Update api model provider details",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/providers/{provider}")
    public ResponseEntity<String> saveProvider(
            @PathVariable String provider,
            @RequestBody SaveProviderRequest request) {

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
            summary = "Set active provider",
            description = "Set athe api model prover to use for AI requests",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/active")
    public ResponseEntity<String> setActive(@RequestBody SetActiveRequest request) {
        aiConfigService.setActiveProvider(request.provider(), request.model());
        return ResponseEntity.ok("Active model updated");
    }

    // ---------- Status ----------
    @Operation(
            summary = "Get the api provider's status",
            description = "Get the api provider's status",
            security = @SecurityRequirement(name = "bearerAuth")
    )
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
    
    @Operation(summary = "List supported AI providers with recommended models",
            description = "List supported AI providers with recommended models",
            security = @SecurityRequirement(name = "bearerAuth"))
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
