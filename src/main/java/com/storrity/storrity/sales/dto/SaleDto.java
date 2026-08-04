/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.sales.entity.PckQtyWithSellinPrice;
import com.storrity.storrity.sales.entity.Sale;
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
@Schema(description = "Sale response object")
public class SaleDto {
    private UUID id;
    private String transactionRef;
    private UUID productId;
    private String productName;
    private String productCode;
    private String productCategory;
    private String productSubCategory;
    private String productBrand;
    private UUID storeId;
    private String storeName;
    private String performedBy;
    private UUID customerId;
    private String customerName;
    private String clientSystemId;
    private String clientSystemName;
    private Double quantity;
    private String sku;
    private List<PckQtyWithSellinPrice> pckQty;
    private Money unitPrice;
    private Double discountRate;
    private Money preDiscountPrice;
    private Money discountAmount;
    private Money discountedAmount;
    private Money amount;
    private Double taxRate;
    private Money taxAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; 
    
    public static SaleDto from(Sale s){
        return SaleDto.builder()
                .amount(s.getAmount())
                .createdAt(s.getCreatedAt())
                .discountAmount(s.getDiscountAmount())
                .discountedAmount(s.getDiscountedAmount())
                .discountRate(s.getDiscountRate())
                .id(s.getId())
                .pckQty(s.getPckQty())
                .performedBy(s.getPerformedBy())
                .customerId(s.getCustomerId())
                .customerName(s.getCustomerName())
                .clientSystemId(s.getClientSystemId())
                .clientSystemName(s.getClientSystemName())
                .preDiscountPrice(s.getPreDiscountPrice())
                .productId(s.getProductId())
                .productName(s.getProductName())
                .productCode(s.getProductCode())
                .productCategory(s.getProductCategory())
                .productSubCategory(s.getProductSubCategory())
                .productBrand(s.getProductBrand())
                .quantity(s.getQuantity())
                .sku(s.getSku())
                .storeId(s.getStoreId())
                .storeName(s.getStoreName())
                .taxAmount(s.getTaxAmount())
                .taxRate(s.getTaxRate())
                .transactionRef(s.getTransactionRef())
                .unitPrice(s.getUnitPrice())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}
