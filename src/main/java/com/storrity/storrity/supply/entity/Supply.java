/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.util.approval.ApprovalStatus;
import com.storrity.storrity.util.entity.MetadataConverter;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.Map;
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
@Table(name = "supply")
public class Supply {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String transactionRef;
    private UUID storeId;
    private String storeName;
    
    @NotNull
    @Column(name = "supply_status")
    @Enumerated(EnumType.STRING)
    private SupplyStatus supplyStatus;
    private String deliveryNoteNumber;
    private String invoiceNumber;
    @NotNull
    @Column(name = "most_recent_supply_action")
    @Enumerated(EnumType.STRING)
    private SupplyAction mostRecentSupplyAction;

    private String supplierId;
    private String supplierName;
    private String contactPerson;
    private String supplierPhone;
    private String supplierEmail;
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "grand_total_in_micro_naira"))
    })   
    private Money grandTotal;    
    private String notes; 
    
    @OneToMany(mappedBy = "supplyId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<OrderItem> orderItems;   
    @OneToMany(mappedBy = "supplyId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<SupplyItem> supplyItems;    
    
    @Convert(converter = MetadataConverter.class)
    private Map<String, Object> metadata; 
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Embedded
    private SupplyProcess supplyProcess;
    @Embedded
    private SupplyTimeline supplyTimeline;
    
    
//    Supply date is the date supply is expected
    private LocalDate expectedSupplyDate;

    private LocalDateTime purchaseOrderSubmittedAt;
    private LocalDateTime purchaseOrderApprovedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime deliverySubmittedAt;
    private LocalDateTime deliveryApprovedAt;
    private LocalDateTime receivedAt; //    Received at is the time the order is received
    private LocalDateTime canceledAt;    
    
    private String createdBy;
    private String draftSubmittedBy;
    private String draftApprovedBy;
    private String deliveryBy;
    private String deliverySbmittedBy;
    private String deliveryApprovedBy;
    private String receivedBy;
    private String cancledBy;
    
//  Reporting properties are derive these from expectedSupplyDate
    private LocalDate reportingExpectedSupplyDate;
    private Integer reportingExpectedSupplyDayOfMonth;
    private Integer reportingExpectedSupplyDayOfWeek;
    private Integer reportingExpectedSupplyWeek;
    private LocalDate reportingExpectedSupplyWeekStartDate;
    private Integer reportingExpectedSupplyMonth;
    private LocalDate reportingExpectedSupplyMonthStartDate;
    private Integer reportingExpectedSupplyQuarter;
    private LocalDate reportingExpectedSupplyQuarterStartDate;
    private Integer reportingExpectedSupplyYear;
    private Integer reportingExpectedSupplyDayOfYear;
    
//  Reporting properties are derive these from draftSubmittedAt
    private LocalDate reportingDraftSubmittedDate;
    private Integer reportingDraftSubmittedHour;
    private Integer reportingDraftSubmittedDayOfMonth;
    private Integer reportingDraftSubmittedDayOfWeek;
    private Integer reportingDraftSubmittedWeek;
    private LocalDate reportingDraftSubmittedWeekStartDate;
    private Integer reportingDraftSubmittedMonth;
    private LocalDate reportingDraftSubmittedMonthStartDate;
    private Integer reportingDraftSubmittedQuarter;
    private LocalDate reportingDraftSubmittedQuarterStartDate;
    private Integer reportingDraftSubmittedYear;
    private Integer reportingDraftSubmittedDayOfYear;
    
//  Reporting properties are derive these from draftApprovedAt
    private LocalDate reportingDraftApprovedDate;
    private Integer reportingDraftApprovedHour;
    private Integer reportingDraftApprovedDayOfMonth;
    private Integer reportingDraftApprovedDayOfWeek;
    private Integer reportingDraftApprovedWeek;
    private LocalDate reportingDraftApprovedWeekStartDate;
    private Integer reportingDraftApprovedMonth;
    private LocalDate reportingDraftApprovedMonthStartDate;
    private Integer reportingDraftApprovedQuarter;
    private LocalDate reportingDraftApprovedQuarterStartDate;
    private Integer reportingDraftApprovedYear;
    private Integer reportingDraftApprovedDayOfYear;
    
//  Reporting properties are derive these from deliveredAt    
    private LocalDate reportingDeliveredDate;
    private Integer reportingDeliveredHour;
    private Integer reportingDeliveredDayOfMonth;
    private Integer reportingDeliveredDayOfWeek;
    private Integer reportingDeliveredWeek;
    private LocalDate reportingDeliveredWeekStartDate;
    private Integer reportingDeliveredMonth;
    private LocalDate reportingDeliveredMonthStartDate;
    private Integer reportingDeliveredQuarter;
    private LocalDate reportingDeliveredQuarterStartDate;
    private Integer reportingDeliveredYear;
    private Integer reportingDeliveredDayOfYear;
    
//  Reporting properties are derive these from deliverySubmittedAt
    private LocalDate reportingDeliverySubmittedDate;
    private Integer reportingDeliverySubmittedHour;
    private Integer reportingDeliverySubmittedDayOfMonth;
    private Integer reportingDeliverySubmittedDayOfWeek;
    private Integer reportingDeliverySubmittedWeek;
    private LocalDate reportingDeliverySubmittedWeekStartDate;
    private Integer reportingDeliverySubmittedMonth;
    private LocalDate reportingDeliverySubmittedMonthStartDate;
    private Integer reportingDeliverySubmittedQuarter;
    private LocalDate reportingDeliverySubmittedQuarterStartDate;
    private Integer reportingDeliverySubmittedYear;
    private Integer reportingDeliverySubmittedDayOfYear;
    
//  Reporting properties are derive these from deliveryApprovedAt
    private LocalDate reportingDeliveryApprovedDate;
    private Integer reportingDeliveryApprovedHour;
    private Integer reportingDeliveryApprovedDayOfMonth;
    private Integer reportingDeliveryApprovedDayOfWeek;
    private Integer reportingDeliveryApprovedWeek;
    private LocalDate reportingDeliveryApprovedWeekStartDate;
    private Integer reportingDeliveryApprovedMonth;
    private LocalDate reportingDeliveryApprovedMonthStartDate;
    private Integer reportingDeliveryApprovedQuarter;
    private LocalDate reportingDeliveryApprovedQuarterStartDate;
    private Integer reportingDeliveryApprovedYear;
    private Integer reportingDeliveryApprovedDayOfYear;
    
//  Reporting properties are derive these from receivedAt
    private LocalDate reportingReceivedDate;
    private Integer reportingReceivedHour;
    private Integer reportingReceivedDayOfMonth;
    private Integer reportingReceivedDayOfWeek;
    private Integer reportingReceivedWeek;
    private LocalDate reportingReceivedWeekStartDate;
    private Integer reportingReceivedMonth;
    private LocalDate reportingReceivedMonthStartDate;
    private Integer reportingReceivedQuarter;
    private LocalDate reportingReceivedQuarterStartDate;
    private Integer reportingReceivedYear;
    private Integer reportingReceivedDayOfYear;
    
//  Reporting properties are derive these from canceledAt
    private LocalDate reportingCanceledDate;
    private Integer reportingCanceledHour;
    private Integer reportingCanceledDayOfMonth;
    private Integer reportingCanceledDayOfWeek;
    private Integer reportingCanceledWeek;
    private LocalDate reportingCanceledWeekStartDate;
    private Integer reportingCanceledMonth;
    private LocalDate reportingCanceledMonthStartDate;
    private Integer reportingCanceledQuarter;
    private LocalDate reportingCanceledQuarterStartDate;
    private Integer reportingCanceledYear;
    private Integer reportingCanceledDayOfYear;
       
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
        
//        @Todo consider moving the functions here to the service class
        if(expectedSupplyDate != null){
            populateExpectedSupplyDateReportingFields(expectedSupplyDate);
        }
        
        if(purchaseOrderSubmittedAt != null){
            populatePurchaseOrderSubmittedAtReportingFields(purchaseOrderSubmittedAt);
        }
        
        if(purchaseOrderApprovedAt != null){
            populatePurchaseOrderApprovedAtReportingFields(purchaseOrderApprovedAt);
        }
        
        if(deliveredAt != null){
            populateDeliveredAtReportingFields(deliveredAt);
        }
        
        if(deliverySubmittedAt != null){
            populatedDeliverySubmittedAtReportingFields(deliverySubmittedAt);
        }
        
        if(deliveryApprovedAt != null){
            populateDeliveryApprovedAtReportingFields(deliveryApprovedAt);
        }
        
        if(receivedAt != null){
            populateReceivedAtReportingFields(receivedAt);
        }
        
        if(canceledAt != null){
            populateCanceledAtReportingFields(canceledAt);
        }
    }
    
    private void populateExpectedSupplyDateReportingFields(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int quarter = ((month - 1) / 3) + 1;

        reportingExpectedSupplyDate = date;

        reportingExpectedSupplyDayOfWeek = date.getDayOfWeek().getValue();
        reportingExpectedSupplyDayOfMonth = date.getDayOfMonth();

        reportingExpectedSupplyWeek = date.get(WeekFields.ISO.weekOfWeekBasedYear());
        
//        @Todo consider ways to make start of week configurable
        reportingExpectedSupplyWeekStartDate = date.with(java.time.DayOfWeek.MONDAY);

        reportingExpectedSupplyMonth = date.getMonthValue();

        reportingExpectedSupplyMonthStartDate = date.withDayOfMonth(1);

        reportingExpectedSupplyQuarter = quarter;

//        @Todo consider ways to make start of quarter configurable such that it may not follow the calendar year
        reportingExpectedSupplyQuarterStartDate = switch (quarter) {
            case 1 -> LocalDate.of(year, 1, 1);
            case 2 -> LocalDate.of(year, 4, 1);
            case 3 -> LocalDate.of(year, 7, 1);
            case 4 -> LocalDate.of(year, 10, 1);
            default -> throw new IllegalStateException(
                    "Unexpected quarter: " + reportingExpectedSupplyQuarter);
        };
        
        reportingExpectedSupplyYear = year;

        reportingExpectedSupplyDayOfYear = reportingExpectedSupplyDate.getDayOfYear();
    }
    
    private void populatePurchaseOrderSubmittedAtReportingFields(LocalDateTime dateTime) {

        LocalDate date = dateTime.toLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int quarter = ((month - 1) / 3) + 1;

        reportingDraftSubmittedDate = date;
        reportingDraftSubmittedHour = dateTime.getHour();

        reportingDraftSubmittedDayOfWeek = dateTime.getDayOfWeek().getValue();
        reportingDraftSubmittedDayOfMonth = dateTime.getDayOfMonth();

        reportingDraftSubmittedWeek = dateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
        
//        @Todo consider ways to make start of week configurable
        reportingDraftSubmittedWeekStartDate = date.with(java.time.DayOfWeek.MONDAY);

        reportingDraftSubmittedMonth = dateTime.getMonthValue();

        reportingDraftSubmittedMonthStartDate = date.withDayOfMonth(1);

        reportingDraftSubmittedQuarter = quarter;

//        @Todo consider ways to make start of quarter configurable such that it may not follow the calendar year
        reportingDraftSubmittedQuarterStartDate = switch (quarter) {
            case 1 -> LocalDate.of(year, 1, 1);
            case 2 -> LocalDate.of(year, 4, 1);
            case 3 -> LocalDate.of(year, 7, 1);
            case 4 -> LocalDate.of(year, 10, 1);
            default -> throw new IllegalStateException(
                    "Unexpected quarter: " + reportingDraftSubmittedQuarter);
        };
        
        reportingDraftSubmittedYear = year;

        reportingDraftSubmittedDayOfYear = reportingDraftSubmittedDate.getDayOfYear();
    }
    
    private void populatePurchaseOrderApprovedAtReportingFields(LocalDateTime dateTime) {

        LocalDate date = dateTime.toLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int quarter = ((month - 1) / 3) + 1;

        reportingDraftApprovedDate = date;
        reportingDraftApprovedHour = dateTime.getHour();

        reportingDraftApprovedDayOfWeek = dateTime.getDayOfWeek().getValue();
        reportingDraftApprovedDayOfMonth = dateTime.getDayOfMonth();

        reportingDraftApprovedWeek = dateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
        
//        @Todo consider ways to make start of week configurable
        reportingDraftApprovedWeekStartDate = date.with(java.time.DayOfWeek.MONDAY);

        reportingDraftApprovedMonth = dateTime.getMonthValue();

        reportingDraftApprovedMonthStartDate = date.withDayOfMonth(1);

        reportingDraftApprovedQuarter = quarter;

//        @Todo consider ways to make start of quarter configurable such that it may not follow the calendar year
        reportingDraftApprovedQuarterStartDate = switch (quarter) {
            case 1 -> LocalDate.of(year, 1, 1);
            case 2 -> LocalDate.of(year, 4, 1);
            case 3 -> LocalDate.of(year, 7, 1);
            case 4 -> LocalDate.of(year, 10, 1);
            default -> throw new IllegalStateException(
                    "Unexpected quarter: " + reportingDraftApprovedQuarter);
        };
        
        reportingDraftApprovedYear = year;

        reportingDraftApprovedDayOfYear = reportingDraftApprovedDate.getDayOfYear();
    }
    
    private void populateDeliveredAtReportingFields(LocalDateTime dateTime) {

        LocalDate date = dateTime.toLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int quarter = ((month - 1) / 3) + 1;

        reportingDeliveredDate = date;
        reportingDeliveredHour = dateTime.getHour();

        reportingDeliveredDayOfWeek = dateTime.getDayOfWeek().getValue();
        reportingDeliveredDayOfMonth = dateTime.getDayOfMonth();

        reportingDeliveredWeek = dateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
        
//        @Todo consider ways to make start of week configurable
        reportingDeliveredWeekStartDate = date.with(java.time.DayOfWeek.MONDAY);

        reportingDeliveredMonth = dateTime.getMonthValue();

        reportingDeliveredMonthStartDate = date.withDayOfMonth(1);

        reportingDeliveredQuarter = quarter;

//        @Todo consider ways to make start of quarter configurable such that it may not follow the calendar year
        reportingDeliveredQuarterStartDate = switch (quarter) {
            case 1 -> LocalDate.of(year, 1, 1);
            case 2 -> LocalDate.of(year, 4, 1);
            case 3 -> LocalDate.of(year, 7, 1);
            case 4 -> LocalDate.of(year, 10, 1);
            default -> throw new IllegalStateException(
                    "Unexpected quarter: " + reportingDeliveredQuarter);
        };
        
        reportingDeliveredYear = year;

        reportingDeliveredDayOfYear = reportingDeliveredDate.getDayOfYear();
    }
    
    private void populatedDeliverySubmittedAtReportingFields(LocalDateTime dateTime) {

        LocalDate date = dateTime.toLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int quarter = ((month - 1) / 3) + 1;

        reportingDeliverySubmittedDate = date;
        reportingDeliverySubmittedHour = dateTime.getHour();

        reportingDeliverySubmittedDayOfWeek = dateTime.getDayOfWeek().getValue();
        reportingDeliverySubmittedDayOfMonth = dateTime.getDayOfMonth();

        reportingDeliverySubmittedWeek = dateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
        
//        @Todo consider ways to make start of week configurable
        reportingDeliverySubmittedWeekStartDate = date.with(java.time.DayOfWeek.MONDAY);

        reportingDeliverySubmittedMonth = dateTime.getMonthValue();

        reportingDeliverySubmittedMonthStartDate = date.withDayOfMonth(1);

        reportingDeliverySubmittedQuarter = quarter;

//        @Todo consider ways to make start of quarter configurable such that it may not follow the calendar year
        reportingDeliverySubmittedQuarterStartDate = switch (quarter) {
            case 1 -> LocalDate.of(year, 1, 1);
            case 2 -> LocalDate.of(year, 4, 1);
            case 3 -> LocalDate.of(year, 7, 1);
            case 4 -> LocalDate.of(year, 10, 1);
            default -> throw new IllegalStateException(
                    "Unexpected quarter: " + reportingDeliverySubmittedQuarter);
        };
        
        reportingDeliverySubmittedYear = year;

        reportingDeliverySubmittedDayOfYear = reportingDeliverySubmittedDate.getDayOfYear();
    }
    
    private void populateDeliveryApprovedAtReportingFields(LocalDateTime dateTime) {

        LocalDate date = dateTime.toLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int quarter = ((month - 1) / 3) + 1;

        reportingDeliveryApprovedDate = date;
        reportingDeliveryApprovedHour = dateTime.getHour();

        reportingDeliveryApprovedDayOfWeek = dateTime.getDayOfWeek().getValue();
        reportingDeliveryApprovedDayOfMonth = dateTime.getDayOfMonth();

        reportingDeliveryApprovedWeek = dateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
        
//        @Todo consider ways to make start of week configurable
        reportingDeliveryApprovedWeekStartDate = date.with(java.time.DayOfWeek.MONDAY);

        reportingDeliveryApprovedMonth = dateTime.getMonthValue();

        reportingDeliveryApprovedMonthStartDate = date.withDayOfMonth(1);

        reportingDeliveryApprovedQuarter = quarter;

//        @Todo consider ways to make start of quarter configurable such that it may not follow the calendar year
        reportingDeliveryApprovedQuarterStartDate = switch (quarter) {
            case 1 -> LocalDate.of(year, 1, 1);
            case 2 -> LocalDate.of(year, 4, 1);
            case 3 -> LocalDate.of(year, 7, 1);
            case 4 -> LocalDate.of(year, 10, 1);
            default -> throw new IllegalStateException(
                    "Unexpected quarter: " + reportingDeliveryApprovedQuarter);
        };
        
        reportingDeliveryApprovedYear = year;

        reportingDeliveryApprovedDayOfYear = reportingDeliveryApprovedDate.getDayOfYear();
    }
    
    private void populateReceivedAtReportingFields(LocalDateTime dateTime) {

        LocalDate date = dateTime.toLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int quarter = ((month - 1) / 3) + 1;

        reportingReceivedDate = date;
        reportingReceivedHour = dateTime.getHour();

        reportingReceivedDayOfWeek = dateTime.getDayOfWeek().getValue();
        reportingReceivedDayOfMonth = dateTime.getDayOfMonth();

        reportingReceivedWeek = dateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
        
//        @Todo consider ways to make start of week configurable
        reportingReceivedWeekStartDate = date.with(java.time.DayOfWeek.MONDAY);

        reportingReceivedMonth = dateTime.getMonthValue();

        reportingReceivedMonthStartDate = date.withDayOfMonth(1);

        reportingReceivedQuarter = quarter;

//        @Todo consider ways to make start of quarter configurable such that it may not follow the calendar year
        reportingReceivedQuarterStartDate = switch (quarter) {
            case 1 -> LocalDate.of(year, 1, 1);
            case 2 -> LocalDate.of(year, 4, 1);
            case 3 -> LocalDate.of(year, 7, 1);
            case 4 -> LocalDate.of(year, 10, 1);
            default -> throw new IllegalStateException(
                    "Unexpected quarter: " + reportingReceivedQuarter);
        };
        
        reportingReceivedYear = year;

        reportingReceivedDayOfYear = reportingReceivedDate.getDayOfYear();
    }
    
    private void populateCanceledAtReportingFields(LocalDateTime dateTime) {

        LocalDate date = dateTime.toLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int quarter = ((month - 1) / 3) + 1;

        reportingCanceledDate = date;
        reportingCanceledHour = dateTime.getHour();

        reportingCanceledDayOfWeek = dateTime.getDayOfWeek().getValue();
        reportingCanceledDayOfMonth = dateTime.getDayOfMonth();

        reportingCanceledWeek = dateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
        
//        @Todo consider ways to make start of week configurable
        reportingCanceledWeekStartDate = date.with(java.time.DayOfWeek.MONDAY);

        reportingCanceledMonth = dateTime.getMonthValue();

        reportingCanceledMonthStartDate = date.withDayOfMonth(1);

        reportingCanceledQuarter = quarter;

//        @Todo consider ways to make start of quarter configurable such that it may not follow the calendar year
        reportingCanceledQuarterStartDate = switch (quarter) {
            case 1 -> LocalDate.of(year, 1, 1);
            case 2 -> LocalDate.of(year, 4, 1);
            case 3 -> LocalDate.of(year, 7, 1);
            case 4 -> LocalDate.of(year, 10, 1);
            default -> throw new IllegalStateException(
                    "Unexpected quarter: " + reportingCanceledQuarter);
        };
        
        reportingCanceledYear = year;

        reportingCanceledDayOfYear = reportingCanceledDate.getDayOfYear();
    }
}
