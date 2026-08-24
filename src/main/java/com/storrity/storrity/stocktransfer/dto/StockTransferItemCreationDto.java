package com.storrity.storrity.stocktransfer.dto;

import com.storrity.storrity.stockmovement.entity.PckQty;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class StockTransferItemCreationDto {
    private String productCode;
    private List<PckQty> pckQty;
    private String batchNumber;
    private LocalDate expiryDate;
    private Map<String, Object> metadata;
}
