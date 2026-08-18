/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.production.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.util.entity.MetadataConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

/**
 *
 * @author Seun Owa
 */
@Entity
@Table(
    name = "production_batch",
    indexes = {
        @Index(name = "idx_production_batch_store", columnList = "store_id"),
        @Index(name = "idx_production_batch_date", columnList = "production_date"),
        @Index(name = "idx_production_batch_status", columnList = "status"),
        @Index(name = "idx_production_batch_type", columnList = "production_type")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductionBatch {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "batch_number", nullable = false, unique = true)
    private String batchNumber;

    /**
     * Generic description of what is being produced.
     * Examples:
     * BREAD
     * SOYBEAN_PROCESSING
     * POULTRY
     * FURNITURE
     */
    @NotNull
    @Column(name = "production_type", nullable = false)
    private String productionType;

    private UUID storeId;
    private String storeName;

    private LocalDate productionDate;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductionStatus status;

    private String createdByUserId;
    private String completedByUserId;

    private String notes;

    @Convert(converter = MetadataConverter.class)
    private Map<String, Object> metadata;

    @OneToMany(
        mappedBy = "productionBatchId",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    private Collection<ProductionItem> items;
    
    @OneToMany(
        mappedBy = "productionBatchId",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    private Collection<ProductionWip> wipItems;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    //be careful to update the values of these fields when production items are created to keep aggregated values in synch

    @AttributeOverrides({
        @AttributeOverride(
            name = "valueInMicroNaira",
            column = @Column(name = "total_input_cost_in_micro_naira")
        )
    })
    private Money totalInputCost;

    @AttributeOverrides({
        @AttributeOverride(
            name = "valueInMicroNaira",
            column = @Column(name = "total_output_value_in_micro_naira")
        )
    })
    private Money totalOutputValue;

    @AttributeOverrides({
        @AttributeOverride(
            name = "valueInMicroNaira",
            column = @Column(name = "total_loss_value_in_micro_naira")
        )
    })
    private Money totalLossValue;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        updatedAt = now;

        if (productionDate == null) {
            productionDate = now.toLocalDate();
        }

        if (status == null) {
            status = ProductionStatus.PLANNED;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}