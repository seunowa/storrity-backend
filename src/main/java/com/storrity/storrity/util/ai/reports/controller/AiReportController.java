/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.util.ai.reports.controller;

import com.storrity.storrity.util.ai.reports.dto.AiReportResponse;
import com.storrity.storrity.util.ai.settings.service.AiReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
            summary = "Generate an AI report",
            description = "Accepts a natural language request and returns a structured report containing narrative text and chart-ready data.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public AiReportResponse generateReport(@RequestBody ReportRequest request) {
        return aiReportService.generateReport(request.prompt());
    }

    public record ReportRequest(String prompt) {}
}
