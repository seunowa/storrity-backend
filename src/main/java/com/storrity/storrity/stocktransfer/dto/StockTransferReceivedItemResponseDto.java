package com.storrity.storrity.stocktransfer.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.entity.StockStatus;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stocktransfer.entity.StockTransferReceivedItem;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockTransferReceivedItemResponseDto {
    private UUID id;
    private UUID stockTransferItemId;
    private UUID productId;
    private String productCode;
    private String productName;
    private UUID storeId;
    private String storeName;
    private String batchNumber;
    private LocalDate expiryDate;
    private Double quantityReceived;
    private Double quantityVariance;
    private String baseUnit;
    private List<PckQty> pckQty;
    private Money costPrice;
    private StockStatus stockStatus;
    private Map<String, Object> metadata;

    public static StockTransferReceivedItemResponseDto from(StockTransferReceivedItem x) {
        return StockTransferReceivedItemResponseDto.builder()
            .id(x.getId())
            .stockTransferItemId(x.getStockTransferItemId())
            .productId(x.getProductId())
            .productCode(x.getProductCode())
            .productName(x.getProductName())
            .storeId(x.getStoreId())
            .storeName(x.getStoreName())
            .batchNumber(x.getBatchNumber())
            .expiryDate(x.getExpiryDate())
            .quantityReceived(x.getQuantityReceived())
            .quantityVariance(x.getQuantityVariance())
            .baseUnit(x.getBaseUnit())
            .pckQty(x.getPckQty())
            .costPrice(x.getCostPrice())
            .stockStatus(x.getStockStatus())
            .metadata(x.getMetadata())
            .build();
    }
}
