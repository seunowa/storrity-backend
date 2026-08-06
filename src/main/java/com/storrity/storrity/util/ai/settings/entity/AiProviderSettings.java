/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.util.ai.settings.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 *
 * @author Seun Owa
 */
@Entity
@Table(name = "ai_provider_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiProviderSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * e.g. OPENAI, DEEPSEEK, ANTHROPIC, OLLAMA, AZURE_OPENAI, etc.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String provider;

    /**
     * Encrypted API key
     */
    @Column(nullable = false, length = 1000)
    private String encryptedApiKey;

    /**
     * Optional base URL (needed for DeepSeek, local Ollama, Azure, etc.)
     */
    private String baseUrl;

    /**
     * Default model for this provider (e.g. gpt-4o-mini, deepseek-chat, claude-3-5-sonnet...)
     */
    private String defaultModel;

    private boolean enabled = true;
}
