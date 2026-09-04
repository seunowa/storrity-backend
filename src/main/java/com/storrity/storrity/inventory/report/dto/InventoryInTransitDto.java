package com.storrity.storrity.inventory.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.stocktransfer.entity.StockTransferStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryInTransitDto {
    private UUID transferId;
    private String transactionRef;
    private UUID sourceStoreId;
    private String sourceStoreName;
    private UUID destinationStoreId;
    private String destinationStoreName;
    private UUID productId;
    private String productName;
    private String productCode;
    private String baseUnit;
    private String batchNumber;
    private double quantitySent;
    private Money inventoryValue;
    private StockTransferStatus transferStatus;
    private LocalDateTime sentAt;
}
