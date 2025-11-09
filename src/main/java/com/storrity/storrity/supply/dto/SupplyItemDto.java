/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.supply.entity.SupplyItem;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SupplyItemDto {
    @NotNull
    private UUID productId;
    private String productName;
    private String batchNumber;
    private LocalDate expiryDate;
    @Positive
    private Double quantitySupplied;
    private String sku;
    private List<PckQty> pckQty;
    private Money unitPrice;
    private Double discountRate;
    private Money costPrice;
    
    
    public static SupplyItemDto from(SupplyItem item){
        return SupplyItemDto.builder()
                .batchNumber(item.getBatchNumber())
                .expiryDate(item.getExpiryDate())
                .pckQty(item.getPckQty())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantitySupplied(item.getQuantity())
                .sku(item.getSku())
                .costPrice(item.getCostPrice())
                .build();
    }
}
