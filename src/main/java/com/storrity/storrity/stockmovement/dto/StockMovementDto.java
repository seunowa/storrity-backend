/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.stockmovement.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.entity.StockStatus;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stockmovement.entity.StockMoevmentDirection;
import com.storrity.storrity.stockmovement.entity.StockMovement;
import com.storrity.storrity.stockmovement.entity.StockMovementType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Schema(description = "Stock movement response object")
public class StockMovementDto {
    private UUID id;
    private String description;
    private Double qtyIn; 
    private Double qtyOut;
    private Double balance;
    private String performedBy;
    private String transactionRef;
    private StockMovementType movementType;
    private StockMoevmentDirection direction;
    private UUID storeId;
    private String storeName;
    private UUID productId;
    private String productName;
    private String productCode;
    private String productCategory;
    private String productSubCategory;
    private String productBrand;
    private Money unitCost;
    private Money movementValue;
    private String sku;
    private LocalDateTime createdAt;
    private List<PckQty> pckQty;
    private Map<String, Object> metadata;
    private StockStatus stockStatus;
    
    public static StockMovementDto from(StockMovement sm) {        
        return StockMovementDto.builder()
                .id(sm.getId())
                .description(sm.getDescription())
                .qtyIn(sm.getQtyIn())
                .qtyOut(sm.getQtyOut())
                .balance(sm.getBalance())
                .performedBy(sm.getPerformedBy())
                .transactionRef(sm.getTransactionRef())
                .movementType(sm.getMovementType())
                .direction(sm.getDirection())
                .storeId(sm.getStoreId())
                .storeName(sm.getStoreName())
                .productId(sm.getProductId())
                .productName(sm.getProductName())
                .productCode(sm.getProductCode())
                .productCategory(sm.getProductCategory())
                .productSubCategory(sm.getProductSubCategory())
                .productBrand(sm.getProductBrand())
                .unitCost(sm.getUnitCost())
                .movementValue(sm.getMovementValue())
                .sku(sm.getSku())
                .createdAt(sm.getCreatedAt())
                .pckQty(sm.getPckQty())
                .metadata(sm.getMetadata())
                .stockStatus(sm.getStockStatus())
                .build();
    }
    
    public static List<StockMovementDto> from(StockMovementResult smr) {
        if (smr == null || smr.getStockMovements() == null) {
        return java.util.Collections.emptyList();
        }
        return smr.getStockMovements();
    }
}
