/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cart.dto;

import com.storrity.storrity.cart.entity.Cart;
import com.storrity.storrity.cart.entity.CartItem;
import com.storrity.storrity.cart.entity.CartStatus;
import com.storrity.storrity.store.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
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
@Schema(description = "Cart list response object")
public class CartDto {
    private UUID id;
    private String tag;  
    private String transactionRef;
    private UUID storeId;
    private String storeName;
    private Collection<CartItemDto> items;
    private String customerId;
    private String createdBy;
    private Map<String, Object> metadata;
    private CartStatus cartStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static CartDto from(Cart c){
        
        List<CartItemDto> cartItems = c.getItems()
                .stream()
                .map(CartItemDto::from)
                .collect(Collectors.toList());
        
        return CartDto.builder()
                .id(c.getId())
                .tag(c.getTag())
                .transactionRef(c.getTransactionRef())
                .storeId(c.getStore().getId())
                .storeName(c.getStore().getName())
                .items(cartItems)
                .customerId(c.getCustomerId())
                .createdBy(c.getCreatedBy())
                .metadata(c.getMetadata())
                .cartStatus(c.getCartStatus())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
