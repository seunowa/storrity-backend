/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.stockmovement.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.entity.StockStatus;
import com.storrity.storrity.util.entity.MetadataConverter;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

/**
 *
 * @author Seun Owa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "stock_movement")
public class StockMovement {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name = "qty_in")
    private Double qtyIn;  
    @Column(name = "qty_out")
    @NotNull
    private Double qtyOut;
    @NotNull
    @Column(name = "balance")
    private Double balance;
    private String sku;
//    @Deprecated
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;
    private String performedBy;
    private String transactionRef;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private StockMovementType movementType;
    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private StockMoevmentDirection direction;
    
    private UUID storeId;
    private String storeName;
    
    private UUID productId;
    private String productName;
    private String productCode;
    private String productCategory;
    private String productSubCategory;
    private String productBrand;
    
    private String batchNumber;
    private LocalDate expiryDate;
    
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "unit_cost_in_micro_naira"))
    })
    private Money unitCost;
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "movement_value_in_micro_naira"))
    })
    private Money movementValue;
    
    private LocalDate reportingDate;
    private Integer reportingHour;
    private Integer reportingDayOfMonth;
    private Integer reportingDayOfWeek;
    private Integer reportingWeek;
    private LocalDate reportingWeekStartDate;
    private Integer reportingMonth;
    private LocalDate reportingMonthStartDate;
    private Integer reportingQuarter;
    private LocalDate reportingQuarterStartDate;
    private Integer reportingYear;
    private Integer reportingDayOfYear;    
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Convert(converter = PckQtyConverter.class)
    private List<PckQty> pckQty;
    @Convert(converter = MetadataConverter.class)
    private Map<String, Object> metadata;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status")
    private StockStatus stockStatus;
       
    @PrePersist
    public void prePersist(){        
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        
        populateReportingFields(now);
    }
    
    @PreUpdate
    public void preUpdate(){
        LocalDateTime now = LocalDateTime.now();
        updatedAt = now;
    }
    
    private void populateReportingFields(LocalDateTime dateTime) {

        LocalDate date = dateTime.toLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int quarter = ((month - 1) / 3) + 1;

        reportingDate = date;
        reportingHour = dateTime.getHour();

        reportingDayOfWeek = dateTime.getDayOfWeek().getValue();
        reportingDayOfMonth = dateTime.getDayOfMonth();

        reportingWeek = dateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
        
//        @Todo consider ways to make start of week configurable
        reportingWeekStartDate = date.with(java.time.DayOfWeek.MONDAY);

        reportingMonth = dateTime.getMonthValue();

        reportingMonthStartDate = date.withDayOfMonth(1);

        reportingQuarter = quarter;

//        @Todo consider ways to make start of quarter configurable such that it may not follow the calendar year
        reportingQuarterStartDate = switch (quarter) {
            case 1 -> LocalDate.of(year, 1, 1);
            case 2 -> LocalDate.of(year, 4, 1);
            case 3 -> LocalDate.of(year, 7, 1);
            case 4 -> LocalDate.of(year, 10, 1);
            default -> throw new IllegalStateException(
                    "Unexpected quarter: " + reportingQuarter);
        };
        
        reportingYear = year;

        reportingDayOfYear = reportingDate.getDayOfYear();
    }
}
