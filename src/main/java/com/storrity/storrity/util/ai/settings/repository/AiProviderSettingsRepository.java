/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.util.ai.settings.repository;

import com.storrity.storrity.util.ai.settings.entity.AiProviderSettings;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Seun Owa
 */
public interface AiProviderSettingsRepository extends JpaRepository<AiProviderSettings, Long> {
    Optional<AiProviderSettings> findByProvider(String provider);
    List<AiProviderSettings> findByEnabledTrue();
}
