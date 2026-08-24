/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stocktransfer.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Table(name = "stock_transfer")
public class StockTransfer {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String transactionRef;

    /**
     * Store from which inventory is being transferred.
     */
    @NotNull
    @Column(name = "source_store_id", nullable = false)
    private UUID sourceStoreId;

    private String sourceStoreName;

    /**
     * Store to which inventory is being transferred.
     */
    @NotNull
    @Column(name = "destination_store_id", nullable = false)
    private UUID destinationStoreId;

    private String destinationStoreName;

    @NotNull
    @Column(name = "transfer_status")
    @Enumerated(EnumType.STRING)
    private StockTransferStatus transferStatus;

    @NotNull
    @Column(name = "most_recent_transfer_action")
    @Enumerated(EnumType.STRING)
    private StockTransferAction mostRecentTransferAction;

    /**
     * Items that the source store is sending.
     */
    @OneToMany(
        mappedBy = "stockTransferId",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER
    )
    private Collection<StockTransferItem> itemsToSend;

    /**
     * Items actually received by the destination store.
     */
    @OneToMany(
        mappedBy = "stockTransferId",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER
    )
    private Collection<StockTransferReceivedItem> itemsReceived;

    @AttributeOverrides({
        @AttributeOverride(
            name = "valueInMicroNaira",
            column = @Column(name = "total_value_in_micro_naira")
        )
    })
    private Money totalValue;

    private String notes;

    /**
     * Date by which the transfer is expected to arrive.
     */
    private LocalDate expectedTransferDate;

    @Convert(converter = MetadataConverter.class)
    private Map<String, Object> metadata;

    @Embedded
    private StockTransferProcess stockTransferProcess;

    @Embedded
    private StockTransferTimeline stockTransferTimeline;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /*
     * Action timestamps
     */
    private LocalDateTime draftSubmittedAt;
    private LocalDateTime draftApprovedAt;
    private LocalDateTime sentAt;
    private LocalDateTime receiptSubmittedAt;
    private LocalDateTime receiptApprovedAt;
    private LocalDateTime receivedAt;
    private LocalDateTime canceledAt;

    /*
     * Action actors
     */
    private String createdBy;
    private String draftSubmittedBy;
    private String draftApprovedBy;
    private String sentBy;
    private String receiptSubmittedBy;
    private String receiptApprovedBy;
    private String receivedBy;
    private String canceledBy;
    
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
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (transferStatus == null) {
            transferStatus = StockTransferStatus.DRAFT;
        }

        if (mostRecentTransferAction == null) {
            mostRecentTransferAction = StockTransferAction.DRAFT;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}