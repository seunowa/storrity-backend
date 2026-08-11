/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 *
 * @author Seun Owa
 */
@Component
@RequiredArgsConstructor
public class BusinessContextTools {

    private final BusinessContextService businessContextService;

    @Tool(description = """
        Get the business context used to interpret business data and make business decisions.
        The context contains vision, goals, sales targets, challenges, strategic priorities,
        known issues, operating hours, and additional notes.

        This is a cross-domain context tool and may be relevant to sales, inventory,
        customers, cashiers, accounting, expenses, and other business domains.

        Call this tool when the user's request requires interpretation of business
        performance, comparison with business objectives or targets, anomaly analysis,
        or business recommendations.

        For a specific store, provide its storeId.
        For a whole-business request, omit storeId or pass null.
        """)
    public String getBusinessContext(
            @ToolParam(description = "Store ID if the question is about a specific store. Leave null for whole business.", required = false)
            UUID storeId) {

        return businessContextService.getContextAsText(storeId);
    }
}
