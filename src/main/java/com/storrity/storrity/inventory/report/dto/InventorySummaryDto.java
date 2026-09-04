package com.storrity.storrity.inventory.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventorySummaryDto {
    private long productCount;
    private double totalQuantity;
    private Money totalInventoryValue;
}
