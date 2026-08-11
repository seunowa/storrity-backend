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
public class ChartData {

    @JsonProperty(required = true)
    @JsonPropertyDescription("The rendering type of the chart. Must be lower-case string. E.g., 'line', 'bar', 'pie'")
    private String chartType;

    @JsonProperty(required = true)
    @JsonPropertyDescription("The human-readable clear headline title of the chart summarizing what the data represents")
    private String title;

    @JsonProperty(required = true)
    @JsonPropertyDescription("The metadata block defining properties specifically for the horizontal X-axis")
    private AxisConfig xAxis;

    @JsonProperty(required = true)
    @JsonPropertyDescription("The metadata block defining properties specifically for the vertical Y-axis")
    private AxisConfig yAxis;

    @JsonProperty(required = true)
    @JsonPropertyDescription("An ordered master array of string categories/labels used across the X-axis timeline. E.g., ['Q1', 'Q2', 'Q3', 'Q4']")
    private List<String> categories;

    @JsonProperty(required = true)
    @JsonPropertyDescription("A list containing individual data series trends. Each series object maps explicitly to the categories above using key-value pair objects")
    private List<ChartSeries> series;

    // Getters and Setters
    public String getChartType() { return chartType; }
    public void setChartType(String chartType) { this.chartType = chartType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public AxisConfig getXAxis() { return xAxis; }
    public void setXAxis(AxisConfig xAxis) { this.xAxis = xAxis; }

    public AxisConfig getYAxis() { return yAxis; }
    public void setYAxis(AxisConfig yAxis) { this.yAxis = yAxis; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    public List<ChartSeries> getSeries() { return series; }
    public void setSeries(List<ChartSeries> series) { this.series = series; }
}
