package com.storrity.storrity.stocktransfer.dto;

import com.storrity.storrity.product.entity.StockStatus;
import com.storrity.storrity.stockmovement.entity.PckQty;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

@Data
public class StockTransferReceivedItemDto {
    private UUID stockTransferItemId;
    private String productCode;
    private Double quantityReceived;
    private List<PckQty> pckQty;
    private String batchNumber;
    private LocalDate expiryDate;
    private StockStatus stockStatus;
    private Map<String, Object> metadata;
}
