package com.storrity.storrity.inventory.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
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
public class SlowMovingInventoryDto {
    private UUID productId;
    private String productName;
    private String productCode;
    private String baseUnit;
    private UUID storeId;
    private String storeName;
    private double quantityInStock;
    private Money inventoryValue;
    private LocalDateTime lastMovementAt;
    private LocalDateTime lastStockOutAt;
    private Long daysSinceLastStockOut;
}
