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
    private final ToolCallingManager toolCallingManager;

    public DynamicChatClientFactory(AiConfigService aiConfigService,
                                    SalesReportTools salesReportTools,
                                    ToolCallingManager toolCallingManager) {
        this.aiConfigService = aiConfigService;
        this.salesReportTools = salesReportTools;
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
                .defaultTools(salesReportTools)
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
}