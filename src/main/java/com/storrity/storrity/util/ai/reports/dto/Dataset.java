/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.util.ai.reports.dto;

import java.util.List;

/**
 *
 * @author Seun Owa
 */
public record Dataset(
        String label,
        List<Number> data,     // plain numbers (Money will be converted to BigDecimal by Jackson)
        String backgroundColor,
        String borderColor
) {}
