package com.storrity.storrity.stocktransfer.dto;

import com.storrity.storrity.stocktransfer.entity.StockTransferStatus;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Data
public class StockTransferQueryParams {
    private UUID sourceStoreId;
    private UUID destinationStoreId;
    private UUID storeId;
    private StockTransferStatus status;
    private String productCode;
    private String transactionRef;
    private LocalDate fromDate;
    private LocalDate toDate;
}
