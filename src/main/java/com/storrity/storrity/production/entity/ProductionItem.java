/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.production.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.util.entity.MetadataConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
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
    name = "production_item",
    indexes = {
        @Index(
            name = "idx_production_item_batch",
            columnList = "production_batch_id"
        ),
        @Index(
            name = "idx_production_item_product",
            columnList = "product_id"
        ),
        @Index(
            name = "idx_production_item_type",
            columnList = "item_type"
        ),
        @Index(
            name = "idx_production_item_date",
            columnList = "movement_date"
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductionItem {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "production_batch_id", nullable = false)
//    private ProductionBatch productionBatch;
    private UUID productionBatchId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private ProductionItemType itemType;

    /**
     * Product associated with the production item.
     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    private Product product;

    /**
     * Product snapshots for historical reporting.
     */
    private UUID productId;
    private String productName;
    private String productCode;
    private String baseUnit;
    private String productCategory;
    private String productSubCategory;
    
    private UUID storeId;
    private String storeName;

    /**
     * Quantity entering/leaving production.
     */
    private Double quantity;

    private String unitOfMeasure;

    /**
     * Cost of this production item.
     */
    @AttributeOverrides({
        @AttributeOverride(
            name = "valueInMicroNaira",
            column = @Column(name = "unit_cost_in_micro_naira")
        )
    })
    private Money unitCost;

    @AttributeOverrides({
        @AttributeOverride(
            name = "valueInMicroNaira",
            column = @Column(name = "total_cost_in_micro_naira")
        )
    })
    private Money totalCost;

    /**
     * When this production item occurred.
     *
     * Important for long-running production.
     */
    @Column(name = "movement_date", nullable = false)
    private LocalDateTime movementDate;

    /**
     * Optional reason for a LOSS.
     *
     * Examples:
     * DAMAGE
     * SPOILAGE
     * MORTALITY
     * WASTE
     * BREAKAGE
     */
    private String reason;

    private String performedByUserId;

    private String notes;

    @Convert(converter = MetadataConverter.class)
    private Map<String, Object> metadata;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    private Integer movementYear;
    private Integer movementMonth;
    private Integer movementQuarter;
    private Integer movementWeek;

    private LocalDate movementDateOnly;
    private LocalDate movementWeekStartDate;
    private LocalDate movementMonthStartDate;
    private LocalDate movementQuarterStartDate;

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        updatedAt = now;

        if (movementDate == null) {
            movementDate = now;
        }

        if (totalCost == null
                && unitCost != null
                && quantity != null) {

            totalCost = unitCost.multiply(quantity);
        }
        
        populateReportingFields(now);
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();

        if (unitCost != null && quantity != null) {
            totalCost = unitCost.multiply(quantity);
        }
    }
    
    private void populateReportingFields(LocalDateTime dateTime) {

        LocalDate date = dateTime.toLocalDate();

        this.movementDateOnly = date;
        this.movementYear = date.getYear();
        this.movementMonth = date.getMonthValue();
        this.movementQuarter = ((date.getMonthValue() - 1) / 3) + 1;

        WeekFields weekFields = WeekFields.ISO;

        this.movementWeek =
                date.get(weekFields.weekOfWeekBasedYear());

        this.movementWeekStartDate =
                date.with(DayOfWeek.MONDAY);

        this.movementMonthStartDate =
                date.withDayOfMonth(1);

        this.movementQuarterStartDate =
                date.withMonth(
                    ((date.getMonthValue() - 1) / 3) * 3 + 1
                ).withDayOfMonth(1);
    }
}