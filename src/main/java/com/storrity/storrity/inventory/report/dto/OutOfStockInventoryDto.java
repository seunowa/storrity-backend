package com.storrity.storrity.inventory.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutOfStockInventoryDto {
    private UUID productId;
    private String productName;
    private String productCode;
    private String baseUnit;
    private UUID storeId;
    private String storeName;
    private double quantityInStock;
    private Double reorderLevel;
    private Double reorderQuantity;
    private Money inventoryValue;
}
