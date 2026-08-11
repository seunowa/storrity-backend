/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.reports.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 *
 * @author Seun Owa
 */
public class AxisConfig {

    @JsonProperty(required = true)
    @JsonPropertyDescription("The descriptive visual label displayed on the axis. E.g., 'Fiscal Quarters' or 'Amount (USD)'")
    private String label;

    @JsonProperty(required = false)
    @JsonPropertyDescription("Optional data format token used by the frontend to parse the values. E.g., 'currency', 'percentage', or 'number'")
    private String format;

    // Getters and Setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
}
