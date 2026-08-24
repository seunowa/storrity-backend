package com.storrity.storrity.stocktransfer.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

@Data
public class StockTransferCreationDto {
    private String transactionRef;
    private UUID sourceStoreId;
    private UUID destinationStoreId;
    private LocalDate expectedTransferDate;
    private String notes;
    private Map<String, Object> metadata;
    private List<StockTransferItemCreationDto> items;
}
