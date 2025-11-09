/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cart.dto;

import com.storrity.storrity.cart.entity.CartItem;
import com.storrity.storrity.sales.entity.PckQtyWithSellinPrice;
import com.storrity.storrity.stockmovement.entity.PckQty;
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
public class CartItemDto {    
    private UUID id;
    private UUID cartId;
    private UUID productId;
    private String productName;
    private Double quantity;
    private String sku;
    private List<PckQtyWithSellinPrice> pckQty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static CartItemDto from(CartItem i){
        return CartItemDto.builder()
                .id(i.getId())
                .cartId(i.getCartId())
                .productId(i.getProduct().getId())
                .productName(i.getProduct().getName())
                .quantity(i.getQuantity())
                .sku(i.getSku())
                .pckQty(i.getPckQty())
                .createdAt(i.getCreatedAt())
                .updatedAt(i.getUpdatedAt())
                .build();
    }
}
