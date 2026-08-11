/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.entity;

import com.storrity.storrity.util.entity.MetadataConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

/**
 *
 * @author Seun Owa
 */
@Entity
@Table(name = "business_context")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessContext {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * null = Global (whole business)
     * otherwise = specific store
     */
    private UUID storeId;

    @Column(length = 2000)
    private String vision;

    @Column(length = 2000)
    private String goals;          // e.g. "Increase net sales by 25% in 2026"

    @Column(length = 2000)
    private String salesTargets;          // e.g. monthly/quarterly targets

    @Column(length = 2000)
    private String challenges;         // e.g. "High stockouts in Category X, low conversion in Store Y"

    @Column(length = 2000)
    private String strategicPriorities;   // e.g. "Focus on premium products, expand in Lagos"

    @Column(length = 2000)
    private String knownIssues;           // operational problems

    @Column(length = 3000)
    private String additionalNotes;       // free-form context
    
    // Operating hours – gives report analysis a baseline for what counts as "open"
    @Column(name = "operating_hours", columnDefinition = "TEXT")
    @Convert(converter = OperatingHoursConverter.class)
    private OperatingHours operatingHours;

    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; 
    
    @PrePersist
    public void prePersist(){        
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }
    
    @PreUpdate
    public void preUpdate(){
        LocalDateTime now = LocalDateTime.now();
        updatedAt = now;
    }
    
    
}