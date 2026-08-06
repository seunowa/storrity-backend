/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.util.ai.settings.service;

import com.storrity.storrity.util.ai.reports.dto.AiReportResponse;
import com.storrity.storrity.util.exception.BadRequestAppException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
@RequiredArgsConstructor
public class AiReportService {

    private final AiConfigService aiConfigService;
    private final DynamicChatClientFactory chatClientFactory;

    public AiReportResponse generateReport(String userPrompt) {
        
        if (!aiConfigService.isAnyProviderConfigured()) {
            throw new BadRequestAppException("No AI provider has been configured. Please set an API key first.");
        }
        
        ChatClient chatClient = chatClientFactory.createChatClient();

        String enrichedPrompt = """
                Current date: %s
                
                User request: %s
                """.formatted(java.time.LocalDate.now(), userPrompt);

        return chatClient.prompt()
                .user(enrichedPrompt)
                .call()
                .entity(AiReportResponse.class);
    }
}