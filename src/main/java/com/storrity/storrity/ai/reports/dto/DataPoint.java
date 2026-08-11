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
public class DataPoint {

    @JsonProperty(required = true)
    @JsonPropertyDescription("The category name matching exactly one of the values specified in the primary categories collection. This is the horizontal key marker.")
    private String x;

    @JsonProperty(required = true)
    @JsonPropertyDescription("The numerical metric value associated with this specific category marker. This is the vertical plot coordinate.")
    private Double y;

    // Default constructor needed for Jackson deserialization
    public DataPoint() {}

    public DataPoint(String x, Double y) {
        this.x = x;
        this.y = y;
    }

    // Getters and Setters
    public String getX() { return x; }
    public void setX(String x) { this.x = x; }

    public Double getY() { return y; }
    public void setY(Double y) { this.y = y; }
}
