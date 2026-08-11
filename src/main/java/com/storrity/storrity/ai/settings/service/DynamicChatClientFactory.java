/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.service;

import com.storrity.storrity.sales.report.service.SalesReportTools;
import com.storrity.storrity.ai.settings.entity.AiProviderSettings;
import java.time.LocalDate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;


/**
 *
 * @author Seun Owa
 */
@Service
public class DynamicChatClientFactory {

    private final AiConfigService aiConfigService;
    private final SalesReportTools salesReportTools;   // your existing tools
    private final BusinessContextTools businessContextTools;
    private final ToolCallingManager toolCallingManager;

    public DynamicChatClientFactory(AiConfigService aiConfigService,
                                    SalesReportTools salesReportTools,
                                    BusinessContextTools businessContextTools,
                                    ToolCallingManager toolCallingManager) {
        this.aiConfigService = aiConfigService;
        this.salesReportTools = salesReportTools;
        this.businessContextTools = businessContextTools;
        this.toolCallingManager = toolCallingManager;
    }

    /**
     * Creates a fresh ChatClient using the currently selected provider + key.
     * This is called on every AI request → key changes take effect immediately.
     * @return 
     */
    public ChatClient createChatClient() {
        String provider = aiConfigService.getActiveProvider();
        String model = aiConfigService.getActiveModel();
        AiProviderSettings settings = aiConfigService.getProviderSettings(provider);
        String apiKey = aiConfigService.getDecryptedApiKey(provider);

        ChatModel chatModel = createChatModel(provider, apiKey, settings.getBaseUrl(), model);

        return ChatClient.builder(chatModel)
                .defaultSystem(buildSystemPrompt())
                .defaultTools(salesReportTools, businessContextTools)
                .build();
    }

    private ChatModel createChatModel(String provider, String apiKey, String baseUrl, String model) {
        return switch (provider.toUpperCase()) {
            case "OPENAI", "DEEPSEEK", "GROQ", "OPENROUTER" -> {
                // 1. Build the API client
                OpenAiApi.Builder apiBuilder = OpenAiApi.builder().apiKey(apiKey);

                if (baseUrl != null && !baseUrl.isBlank()) {
                    apiBuilder.baseUrl(baseUrl);
                }

                OpenAiApi openAiApi = apiBuilder.build();

                // 2. Build the options
                OpenAiChatOptions options = OpenAiChatOptions.builder()
                        .model(model)
                        .temperature(0.2)
                        .build();

                // 3. Create the ChatModel
                yield OpenAiChatModel.builder()
                    .openAiApi(openAiApi)
                    .defaultOptions(options)
                    .toolCallingManager(toolCallingManager)
                    .build();
                }
            // Future providers:
            // case "ANTHROPIC" -> new AnthropicChatModel(...);
            // case "OLLAMA"    -> new OllamaChatModel(...);
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }
    
    private String buildSystemPrompt() {
        LocalDate today = LocalDate.now();

        return """
                    You are an expert retail sales analytics assistant for the Storrity store management system.
                    
                    You have access to real sales reporting tools that return accurate aggregated data from the database
                    and Business context tools thet return the current business context (vision, goals, targets, challenges, strategy, known issues, operating hours)
                    
                    When the user asks for a report:
                    1. Decide which tools are needed (you can call multiple tools).
                    2. Call the tools with appropriate date ranges and filters.
                    3. Carefully analyze the returned data.
                    4. Produce a professional structured report.
               
                    RECURSION & INVESTIGATIVE PROBING RULES:
                    - You are not restricted to a single round of tool executions. Use a multi-step "Thought -> Action -> Observation" investigative approach.
                    - Stage 1 (Initial Assessment): Call your primary high-level summary or aggregation tools first.
                    - Stage 2 (Anomaly Detection): Analyze the initial observation. Look closely for concentration anomalies (skewed distributions) or unexpected temporal gaps (prolonged silence/zero activity).
                    - Stage 3 (Deep-Dive Probing): If an anomaly or low-volume bottleneck is spotted, DO NOT write the final report yet. Immediately use your available granular tools (e.g., breakdown by item, hourly logs, or entity statuses) to probe for the root cause. 
                    - Stage 4 (Root Cause Reporting): Summarize the evidence-based investigation by stating the observed anomaly, diagnostic evidence, resulting interpretation, and specific recommendation, clearly distinguishing confirmed findings from hypotheses requiring further investigation.
                    
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
               
                    When comparing two periods:
                    - For each metric, explicitly determine: is current > previous (increase), 
                      current < previous (decrease), or current == previous (no change)?
                    - If the previous period's value is zero and the current period's value is 
                      greater than zero, this is an INCREASE (new activity), never a "drop" or "decline."
                    - Populate the "change" and "trend" field for every keyMetric — never leave 
                       these as "N/A" when both values are known.
                    - Before finalizing your response, re-read your executiveSummary, insights, 
                       and recommendations against the direction you determined. 
                       If any sentence contradicts the computed direction for a metric 
                       (e.g. calling an increase a "drop"), rewrite that sentence.
                    - Dont use negative language (drop, decline, fall, down) to describe a 
                       metric that increased, and never use positive language (growth, up, rise) 
                       to describe a metric that decreased.
                    
                    If the previous period's value is zero:
                    - Do not state a percentage change.
                    - Describe it as "new activity" or state the absolute value only 
                      (e.g. "2 transactions were recorded today, compared to none yesterday").
                                          
                    DATA DIAGNOSTICS & GAP ANALYSIS RULES:
                    - Avoid defaulting to generic, polite, or optimistic language if the overall volume, metric count, or activity levels are critically low or stagnant.
                    - Always evaluate the distribution of the returned data. Check for concentration anomalies (e.g., if a single metric, item, 
                      category, or entity accounts for the entirety of the activity).
                    - Check for chronological or temporal gaps (e.g., prolonged periods, hours, or steps where zero activity or data was recorded).
                    - If the data shows poor performance, severe concentration on a single entity, or long blocks of inactivity, explicitly call 
                      this out as an anomaly or bottleneck in the `executiveSummary`, `sections`, and `insights`.
                    - Your `recommendations` must not be generic or passive. You MUST prescribe specific, investigative actions to diagnose the root causes
                      of the anomaly (e.g., checking for system/process downtime, evaluating unengaged entities, or auditing why other categories recorded zero activity).
                    - Add observed anomaly to                     
               
                    BUSINESS CONTEXT RULE:
                    Business context is a cross-domain interpretation layer. When answering
                    business-performance questions, use the business context tool when the
                    answer could be improved by knowing the business's goals, targets,
                    priorities, challenges, known issues, or operating conditions.

                    For store-specific questions, call the business context tool with the
                    storeId. For whole-business questions, call it without storeId.

                    When business context is relevant to interpreting results or making
                    recommendations, you MUST retrieve it before producing the final answer.
                    When both company-wide and store-specific objectives are relevant, call the tool twice: once for GLOBAL
                    context and once with the storeId.

                    Always convert relative dates into concrete start and end dates when calling the tools.
                    
                    """.formatted(today, today.getDayOfWeek());
//@Todo
//Consider how to handle the case when the proppt covers/includs a period in the future for which no data exists
//for example:
//"prompt": "Prepare the performance report for current year highlighting quarterly contribution"
//
//result:
//{
//  "title": "2026 Quarterly Performance Report",
//  "executiveSummary": "The performance report for the current year shows that the third quarter has recorded net sales, while the first half of the year shows no activity.",
//  "keyMetrics": [
//    {
//      "label": "Q3 2026 Net Sales",
//      "value": "₦7,256.25",
//      "change": "new activity",
//      "trend": "increased"
//    },
//    {
//      "label": "Q1 2026 Net Sales",
//      "value": "₦0.00",
//      "change": "no change",
//      "trend": "no activity"
//    },
//    {
//      "label": "Q2 2026 Net Sales",
//      "value": "₦0.00",
//      "change": "no change",
//      "trend": "no activity"
//    },
//    {
//      "label": "Q4 2025 Net Sales",
//      "value": "₦0.00",
//      "change": "no change",
//      "trend": "no activity"
//    }
//  ],
//  "sections": [
//    {
//      "heading": "Quarterly Sales Overview",
//      "content": "The report highlights the quarterly contributions to net sales for the year 2026, indicating a need for strategic interventions to enhance sales performance."
//    }
//  ],
//  "charts": {
//    "chartType": "bar",
//    "title": "Quarterly Contribution to Net Sales for 2026",
//    "xAxis": {
//      "label": "Fiscal Quarters",
//      "format": "string"
//    },
//    "yAxis": {
//      "label": "Amount (₦)",
//      "format": "currency"
//    },
//    "categories": [
//      "Q4 2025",
//      "Q1 2026",
//      "Q2 2026",
//      "Q3 2026"
//    ],
//    "series": [
//      {
//        "name": "Net Sales",
//        "color": "#3498db",
//        "data": [
//          {
//            "x": "Q4 2025",
//            "y": 0
//          },
//          {
//            "x": "Q1 2026",
//            "y": 0
//          },
//          {
//            "x": "Q2 2026",
//            "y": 0
//          },
//          {
//            "x": "Q3 2026",
//            "y": 7256.25
//          }
//        ]
//      }
//    ],
//    "yaxis": {
//      "label": "Amount (₦)",
//      "format": "currency"
//    },
//    "xaxis": {
//      "label": "Fiscal Quarters",
//      "format": "string"
//    }
//  },
//  "insights": [
//    "Only the third quarter has generated net sales of ₦7,256.25.",
//    "The first and second quarters have recorded no transactions.",
//    "There is a significant gap in sales activity for the first half of the year."
//  ],
//  "recommendations": [
//    "Investigate the lack of sales activity in Q1 and Q2 2026.",
//    "Consider promotional strategies to boost sales in the upcoming quarters.",
//    "Analyze customer engagement and market conditions during the first half of the year."
//  ]
//}
    }
}