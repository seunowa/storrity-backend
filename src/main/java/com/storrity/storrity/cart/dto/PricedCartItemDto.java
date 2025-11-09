/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cart.dto;

import com.storrity.storrity.cart.entity.CartItem;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.sales.entity.PckQtyWithSellinPrice;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.util.dto.ItemPrice;
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
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class PricedCartItemDto {
    private UUID id;
    private UUID cartId;
    private UUID productId;
    private String productName;
    private Double quantity;
    private String sku;
    private List<PckQtyWithSellinPrice> pckQty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Money unitPrice;
    private Double discountRate;
    private Money preDiscountPrice;
    private Money discountAmount;
    private Money discountedPrice;
    private Money sum;
    private Double taxRate;
    private Money taxAmount;
    
    
    public static PricedCartItemDto from(CartItem i, ItemPrice<CartItem> ip){
        return PricedCartItemDto.builder()
            .id(i.getId())
            .cartId(i.getCartId())
            .productId(i.getProduct().getId())
            .productName(i.getProduct().getName())
            .quantity(i.getQuantity())
            .sku(i.getSku())
            .pckQty(i.getPckQty())
            .createdAt(i.getCreatedAt())
            .updatedAt(i.getUpdatedAt())
            .unitPrice(ip.getUnitPrice())
            .discountRate(ip.getDiscountRate())
            .preDiscountPrice(ip.getPreDiscountAmount())
            .discountAmount(ip.getDiscountAmount())
            .discountedPrice(ip.getDiscountedPrice())
            .sum(ip.getSum())
            .taxRate(ip.getTaxRate())
            .taxAmount(ip.getTaxAmount())
            .build();
    }
}
