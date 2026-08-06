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
public record ChartDefinition(
        String id,
        String type,           // "bar", "line", "pie", "doughnut", "area"
        String title,
        String description,
        List<String> labels,
        List<Dataset> datasets
) {

}
