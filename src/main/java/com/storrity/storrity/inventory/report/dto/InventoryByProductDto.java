package com.storrity.storrity.inventory.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.entity.ProductType;
import com.storrity.storrity.product.entity.StockStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryByProductDto {
    private UUID productId;
    private String productName;
    private String productCode;
    private String baseUnit;
    private UUID storeId;
    private String storeName;
    private String category;
    private String subcategory;
    private String brand;
    private ProductType productType;
    private StockStatus stockStatus;
    private double quantityInStock;
    private Money unitPrice;
    private Money inventoryValue;
}
