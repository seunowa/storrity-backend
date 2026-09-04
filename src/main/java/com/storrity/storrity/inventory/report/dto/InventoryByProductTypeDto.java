package com.storrity.storrity.inventory.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.entity.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryByProductTypeDto {
    private ProductType productType;
    private long productCount;
    private double totalQuantity;
    private Money inventoryValue;
}
