/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.service;

import com.storrity.storrity.ai.settings.entity.AiActiveConfig;
import com.storrity.storrity.ai.settings.entity.AiProviderSettings;
import com.storrity.storrity.ai.settings.repository.AiActiveConfigRepository;
import com.storrity.storrity.ai.settings.repository.AiProviderSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author Seun Owa
 */
@Service
@RequiredArgsConstructor
public class AiConfigService {

    private final AiProviderSettingsRepository providerRepo;
    private final AiActiveConfigRepository activeConfigRepo;
    private final EncryptionService encryptionService;

    // ---------- Provider Keys ----------

    @Transactional
    public void saveProviderKey(String provider, String apiKey, String baseUrl, String defaultModel) {
        AiProviderSettings settings = providerRepo.findByProvider(provider.toUpperCase())
                .orElse(AiProviderSettings.builder().provider(provider.toUpperCase()).build());

        settings.setEncryptedApiKey(encryptionService.encrypt(apiKey.trim()));
        settings.setBaseUrl(baseUrl);
        settings.setDefaultModel(defaultModel);
        settings.setEnabled(true);

        providerRepo.save(settings);
    }

    public String getDecryptedApiKey(String provider) {
        AiProviderSettings settings = providerRepo.findByProvider(provider.toUpperCase())
                .orElseThrow(() -> new IllegalStateException("Provider " + provider + " is not configured"));

        if (!settings.isEnabled()) {
            throw new IllegalStateException("Provider " + provider + " is disabled");
        }

        return encryptionService.decrypt(settings.getEncryptedApiKey());
    }

    public AiProviderSettings getProviderSettings(String provider) {
        return providerRepo.findByProvider(provider.toUpperCase())
                .orElseThrow(() -> new IllegalStateException("Provider not configured: " + provider));
    }

    public List<AiProviderSettings> getAllEnabledProviders() {
        return providerRepo.findByEnabledTrue();
    }

    // ---------- Active Model Selection ----------

    @Transactional
    public void setActiveProvider(String provider, String model) {
        // ensure provider exists
        getProviderSettings(provider);

        AiActiveConfig config = activeConfigRepo.findById(1L)
                .orElse(AiActiveConfig.builder().id(1L).build());

        config.setActiveProvider(provider.toUpperCase());
        config.setActiveModel(model);
        activeConfigRepo.save(config);
    }

    public AiActiveConfig getActiveConfig() {
        return activeConfigRepo.findById(1L)
                .orElse(AiActiveConfig.builder()
                        .id(1L)
                        .activeProvider("OPENAI")
                        .build());
    }

    public String getActiveProvider() {
        return getActiveConfig().getActiveProvider();
    }

    public String getActiveModel() {
        AiActiveConfig config = getActiveConfig();
        if (config.getActiveModel() != null && !config.getActiveModel().isBlank()) {
            return config.getActiveModel();
        }
        // fall back to provider default
        return getProviderSettings(config.getActiveProvider()).getDefaultModel();
    }
    
    public boolean isAnyProviderConfigured() {
        return !providerRepo.findByEnabledTrue().isEmpty();
    }
}
