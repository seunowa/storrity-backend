/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.storrity.storrity.ai.settings.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Seun Owa
 */
@Getter
@RequiredArgsConstructor
public enum AiProvider {

    OPENAI(
            "OpenAI",
            null,                                    // baseUrl (null = official)
            "gpt-4o-mini",
            "Official OpenAI models"
    ),

    DEEPSEEK(
            "DeepSeek",
            "https://api.deepseek.com",
            "deepseek-chat",
            "Very cost-effective Chinese model (OpenAI-compatible)"
    ),

    GROQ(
            "Groq",
            "https://api.groq.com/openai/v1",
            "llama-3.3-70b-versatile",
            "Extremely fast inference"
    ),

    OPENROUTER(
            "OpenRouter",
            "https://openrouter.ai/api/v1",
            "openai/gpt-4o-mini",
            "Access many models through one API"
    );

    private final String displayName;
    private final String defaultBaseUrl;
    private final String recommendedModel;
    private final String description;
}
