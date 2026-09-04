package com.storrity.storrity.inventory.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTurnoverDto {
    private UUID storeId;
    private String storeName;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Money costOfGoodsSold;
    private Money averageInventoryValue;
    private Double turnoverRate;
}
