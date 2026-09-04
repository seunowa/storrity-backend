/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.production.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.util.entity.MetadataConverter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 *
 * @author Seun Owa
 */
@Entity
@Table(
    name = "production_wip",
    indexes = {
        @Index(name = "idx_wip_batch", columnList = "production_batch_id"),
        @Index(name = "idx_wip_parent", columnList = "parent_id"),
        @Index(name = "idx_wip_thread", columnList = "thread_id"),
        @Index(name = "idx_wip_status", columnList = "status")
    }
)
public class ProductionWip {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "production_batch_id", nullable = false)
//    private ProductionBatch productionBatch;
    private UUID productionBatchId;

    /**
     * Immediate previous state.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ProductionWip parent;

    /**
     * Identifies the complete transformation lineage.
     */
    @Column(name = "thread_id", nullable = false)
    private UUID threadId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    private Product product;

    private UUID productId;
    private String productName;
    private String productCode;
    private String baseUnit;

    /**
     * Quantity represented by this WIP state.
     */
    private Double quantity;

    private String unitOfMeasure;

    /**
     * Current carrying value of this state.
     */
    @AttributeOverrides({
        @AttributeOverride(
            name = "valueInMicroNaira",
            column = @Column(name = "value_in_micro_naira")
        )
    })
    private Money value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductionWipStatus status;

    /**
     * Flexible state characteristics.
     */
    @Convert(converter = MetadataConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> metadata;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        updatedAt = now;

        if (startedAt == null) {
            startedAt = now;
        }

        if (status == null) {
            status = ProductionWipStatus.IN_PROGRESS;
        }
    }
}
