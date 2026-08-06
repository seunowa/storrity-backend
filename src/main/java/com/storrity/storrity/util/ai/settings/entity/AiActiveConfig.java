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
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ai_active_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiActiveConfig {

    @Id
    private Long id = 1L;          // single row (singleton)

    /**
     * Currently selected provider (OPENAI, DEEPSEEK, etc.)
     */
    @Column(nullable = false)
    private String activeProvider = "OPENAI";

    /**
     * Optional override of the model (if null → use provider's defaultModel)
     */
    private String activeModel;
}
