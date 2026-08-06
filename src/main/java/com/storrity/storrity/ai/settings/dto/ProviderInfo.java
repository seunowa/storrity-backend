/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.dto;

/**
 *
 * @author Seun Owa
 */
public record ProviderInfo(
        String provider,           // OPENAI, DEEPSEEK, ...
        String displayName,
        String defaultBaseUrl,
        String recommendedModel,
        String description
) {}