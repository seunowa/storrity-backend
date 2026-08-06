/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.storrity.storrity.ai.reports.dto;

/**
 *
 * @author Seun Owa
 */
public record KeyMetric(
        String label,
        String value,          // already formatted, e.g. "₦1,234,567.89"
        String change,         // e.g. "+12.4%" or null
        String trend           // "up", "down", "neutral"
) {}
