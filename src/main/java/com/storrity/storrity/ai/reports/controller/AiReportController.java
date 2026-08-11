/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.reports.controller;

import com.storrity.storrity.ai.reports.dto.AiReportResponse;
import com.storrity.storrity.ai.settings.service.AiReportService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Seun Owa
 */
@RestController
@RequestMapping("/api/v1/ai/reports")
@Tag(name = "AI Reports", description = "AI-powered report generation")
@CrossOrigin
public class AiReportController {

    private final AiReportService aiReportService;

    @Autowired
    public AiReportController(AiReportService aiReportService) {
        this.aiReportService = aiReportService;
    }

    @Operation(
            operationId = "generateAiReport",
            summary = "Generate an AI report",
            description = "Accepts a natural language request and returns a structured report containing narrative text and chart-ready data.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "AI report generated successfully",
            content = @Content(schema = @Schema(implementation = AiReportResponse.class))
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
    @PostMapping
    public AiReportResponse generateReport(@RequestBody @Valid ReportRequest request) {
        return aiReportService.generateReport(request.prompt());
    }

    public record ReportRequest(String prompt) {}
}
