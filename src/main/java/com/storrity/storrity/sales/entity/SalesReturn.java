/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.entity;

import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stockmovement.entity.PckQtyConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

/**
 *
 * @author Seun Owa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "sales_return")
public class SalesReturn {    
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String transactionRef;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;
    private Double quantity;
    private String sku;
    @Convert(converter = PckQtyWithSellingPriceConverter.class)
    private List<PckQty> pckQty;
    private String reason;
    private String performedBy;
    private UUID productId;
    private String productName;
    private String productCode;
    private String productCategory;
    private String productSubCategory;    
    private String productBrand;
    private UUID storeId;
    private String storeName;
    private UUID customerId;
    private String customerName;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; 
    
    //    Fields included to aid reporting
    private LocalDate reportingDate;
    private Integer reportingHour;
    private Integer reportingDayOfWeek;
    private Integer reportingDayOfMonth;
    private Integer reportingWeek;
    private LocalDate reportingWeekStartDate;
    private Integer reportingMonth;
    private LocalDate reportingMonthStartDate;
    private Integer reportingQuarter;
    private LocalDate reportingQuarterStartDate;
    private Integer reportingYear;
    private Integer reportingDayOfYear;
       
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
