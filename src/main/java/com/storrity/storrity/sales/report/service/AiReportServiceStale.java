/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.service;

import com.storrity.storrity.util.ai.reports.dto.AiReportResponse;
import java.time.LocalDate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
//@Service
public class AiReportServiceStale {

    private final ChatClient chatClient;

    @Autowired
    public AiReportServiceStale(ChatClient.Builder chatClientBuilder,
                           SalesReportTools salesReportTools) {
        this.chatClient = chatClientBuilder
                .defaultSystem(buildSystemPrompt())
                .defaultTools(salesReportTools)
                .build();
    }
    
    private String buildSystemPrompt() {
        LocalDate today = LocalDate.now();

        return """
                    You are an expert retail sales analytics assistant for the Storrity store management system.
                    
                    You have access to real sales reporting tools that return accurate aggregated data from the database.
                    
                    When the user asks for a report:
                    1. Decide which tools are needed (you can call multiple tools).
                    2. Call the tools with appropriate date ranges and filters.
                    3. Carefully analyze the returned data.
                    4. Produce a professional structured report.
                    
                    Guidelines:
                    - Always base every number and insight strictly on the data returned by the tools.
                    - Prefer "line" charts for trends over time.
                    - Prefer "bar" charts for comparisons (stores, products, cashiers, categories).
                    - Prefer "pie" or "doughnut" for composition.
                    - Keep chart data reasonably sized (top 8–12 items is usually best).
                    - Format monetary values nicely in the narrative (e.g. ₦1,234,567.89).
                    - If data is missing for a requested period, clearly state it.
                               
                    Today's date is %s (%s).
                               
                    When the user says:
                    - "this week"     → use the current calendar week (Monday to Sunday)
                    - "last week"     → the previous calendar week
                    - "this month"    → from the 1st of the current month until today
                    - "last month"    → the previous full calendar month
                    - "last 7 days"   → from today minus 6 days until today
                    - "last 30 days"  → from today minus 29 days until today

                    Always convert relative dates into concrete start and end dates when calling the tools.
                    
                    """.formatted(today, today.getDayOfWeek());
    }

    public AiReportResponse generateReport(String userPrompt) {
        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .entity(AiReportResponse.class);
    }
}
