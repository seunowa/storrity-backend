package com.storrity.storrity.stocktransfer.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.stocktransfer.entity.StockTransfer;
import com.storrity.storrity.stocktransfer.entity.StockTransferAction;
import com.storrity.storrity.stocktransfer.entity.StockTransferStatus;
import com.storrity.storrity.stocktransfer.entity.StockTransferTimelineEntry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockTransferDto {
    private UUID id;
    private String transactionRef;
    private UUID sourceStoreId;
    private String sourceStoreName;
    private UUID destinationStoreId;
    private String destinationStoreName;
    private StockTransferStatus transferStatus;
    private StockTransferAction mostRecentTransferAction;
    private Money totalValue;
    private String notes;
    private LocalDate expectedTransferDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime draftSubmittedAt;
    private LocalDateTime draftApprovedAt;
    private LocalDateTime sentAt;
    private LocalDateTime receiptSubmittedAt;
    private LocalDateTime receiptApprovedAt;
    private LocalDateTime receivedAt;
    private LocalDateTime canceledAt;
    private String createdBy;
    private String draftSubmittedBy;
    private String draftApprovedBy;
    private String sentBy;
    private String receiptSubmittedBy;
    private String receiptApprovedBy;
    private String receivedBy;
    private String canceledBy;
    private List<StockTransferItemDto> itemsToSend;
    private List<StockTransferReceivedItemResponseDto> itemsReceived;
    private List<StockTransferTimelineEntry> timeline;
    private Map<String, Object> metadata;

    public static StockTransferDto from(StockTransfer x) {
        return StockTransferDto.builder()
            .id(x.getId())
            .transactionRef(x.getTransactionRef())
            .sourceStoreId(x.getSourceStoreId())
            .sourceStoreName(x.getSourceStoreName())
            .destinationStoreId(x.getDestinationStoreId())
            .destinationStoreName(x.getDestinationStoreName())
            .transferStatus(x.getTransferStatus())
            .mostRecentTransferAction(x.getMostRecentTransferAction())
            .totalValue(x.getTotalValue())
            .notes(x.getNotes())
            .expectedTransferDate(x.getExpectedTransferDate())
            .createdAt(x.getCreatedAt())
            .updatedAt(x.getUpdatedAt())
            .draftSubmittedAt(x.getDraftSubmittedAt())
            .draftApprovedAt(x.getDraftApprovedAt())
            .sentAt(x.getSentAt())
            .receiptSubmittedAt(x.getReceiptSubmittedAt())
            .receiptApprovedAt(x.getReceiptApprovedAt())
            .receivedAt(x.getReceivedAt())
            .canceledAt(x.getCanceledAt())
            .createdBy(x.getCreatedBy())
            .draftSubmittedBy(x.getDraftSubmittedBy())
            .draftApprovedBy(x.getDraftApprovedBy())
            .sentBy(x.getSentBy())
            .receiptSubmittedBy(x.getReceiptSubmittedBy())
            .receiptApprovedBy(x.getReceiptApprovedBy())
            .receivedBy(x.getReceivedBy())
            .canceledBy(x.getCanceledBy())
            .itemsToSend(x.getItemsToSend() == null ? List.of() :
                x.getItemsToSend().stream().map(StockTransferItemDto::from).collect(Collectors.toList()))
            .itemsReceived(x.getItemsReceived() == null ? List.of() :
                x.getItemsReceived().stream().map(StockTransferReceivedItemResponseDto::from).collect(Collectors.toList()))
            .timeline(x.getStockTransferTimeline() == null || x.getStockTransferTimeline().getItems() == null
                ? List.of() : x.getStockTransferTimeline().getItems())
            .metadata(x.getMetadata())
            .build();
    }
}
