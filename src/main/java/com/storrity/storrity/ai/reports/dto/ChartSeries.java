/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.reports.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public class ChartSeries {

    @JsonProperty(required = true)
    @JsonPropertyDescription("The distinctive display name or legend key for this specific data series trend. E.g., 'Online Sales'")
    private String name;

    @JsonProperty(required = true)
    @JsonPropertyDescription("A valid 6-character hex color code or rgba string representing this series' theme line or bar block color. E.g., '#3498db'")
    private String color;

    @JsonProperty(required = true)
    @JsonPropertyDescription("An array of explicit key-value coordinate points. If a series is missing data for a specific category, completely omit that coordinate entry from this list.")
    private List<DataPoint> data;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public List<DataPoint> getData() { return data; }
    public void setData(List<DataPoint> data) { this.data = data; }
}
