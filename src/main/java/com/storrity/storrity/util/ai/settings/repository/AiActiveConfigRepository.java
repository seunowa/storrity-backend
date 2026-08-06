/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.util.ai.settings.repository;

import com.storrity.storrity.util.ai.settings.entity.AiActiveConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Seun Owa
 */
public interface AiActiveConfigRepository extends JpaRepository<AiActiveConfig, Long> {
}
