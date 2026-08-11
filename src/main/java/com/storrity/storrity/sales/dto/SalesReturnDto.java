/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.dto;

import com.storrity.storrity.sales.entity.SalesReturn;
import com.storrity.storrity.stockmovement.entity.PckQty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
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
@Schema(description = "Sales return response object")
public class SalesReturnDto {
    private UUID id;
    private String transactionRef;
    private UUID saleId;
    private Double quantity;
    private String sku;
    private List<PckQty> pckQty;
    private String reason;
    private String performedBy;
    private UUID productId;
    private String productName;
    private String productCode;
    private String productCategory;
    private String productSubCategory;    
    private String productBrand;
    private UUID storeId;
    private String storeName;
    private UUID customerId;
    private String customerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; 
    
    public static SalesReturnDto from(SalesReturn s){
        return SalesReturnDto.builder()
                .createdAt(s.getCreatedAt())
                .id(s.getId())
                .pckQty(s.getPckQty())
                .performedBy(s.getPerformedBy())
                .productId(s.getProductId())
                .productName(s.getProductName())
                .productCode(s.getProductCode())
                .productCategory(s.getProductCategory())
                .productSubCategory(s.getProductSubCategory())
                .productBrand(s.getProductBrand())
                .storeId(s.getStoreId())
                .storeName(s.getStoreName())
                .customerId(s.getCustomerId())
                .customerName(s.getCustomerName())
                .reason(s.getReason())
                .quantity(s.getQuantity())
                .reason(s.getReason())
                .saleId(s.getSale().getId())
                .sku(s.getSku())
                .transactionRef(s.getTransactionRef())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}
