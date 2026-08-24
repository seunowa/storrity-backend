package com.storrity.storrity.stocktransfer.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stocktransfer.entity.StockTransferItem;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockTransferItemDto {
    private UUID id;
    private UUID productId;
    private String productCode;
    private String productName;
    private String productCategory;
    private String productSubCategory;
    private UUID storeId;
    private String storeName;
    private String batchNumber;
    private LocalDate expiryDate;
    private Double quantitySent;
    private String baseUnit;
    private List<PckQty> pckQty;
    private Money costPrice;
    private Map<String, Object> metadata;

    public static StockTransferItemDto from(StockTransferItem x) {
        return StockTransferItemDto.builder()
            .id(x.getId())
            .productId(x.getProductId())
            .productCode(x.getProductCode())
            .productName(x.getProductName())
            .productCategory(x.getProductCategory())
            .productSubCategory(x.getProductSubCategory())
            .storeId(x.getStoreId())
            .storeName(x.getStoreName())
            .batchNumber(x.getBatchNumber())
            .expiryDate(x.getExpiryDate())
            .quantitySent(x.getQuantitySent())
            .baseUnit(x.getBaseUnit())
            .pckQty(x.getPckQty())
            .costPrice(x.getCostPrice())
            .metadata(x.getMetadata())
            .build();
    }
}
