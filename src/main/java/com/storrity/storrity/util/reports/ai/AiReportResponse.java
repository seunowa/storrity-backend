/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.storrity.storrity.util.reports.ai;

import java.util.List;

/**
 *
 * @author Seun Owa
 */
public record AiReportResponse(
        String title,
        String executiveSummary,
        List<KeyMetric> keyMetrics,
        List<ReportSection> sections,
        List<ChartDefinition> charts,
        List<String> insights,
        List<String> recommendations
) {}
