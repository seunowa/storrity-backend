/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cart.dto;

import com.storrity.storrity.cart.entity.Cart;
import com.storrity.storrity.cart.entity.CartItem;
import com.storrity.storrity.cart.entity.CartStatus;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.util.dto.ItemPrice;
import com.storrity.storrity.util.dto.PriceHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Schema(description = "Cart response object")
public class PricedCartDto {
    private UUID id;
    private String tag;  
    private String transactionRef;
    private UUID storeId;
    private String storeName;
    private Collection<PricedCartItemDto> items;
    private String customerId;
    private String createdBy;
    private Map<String, Object> metadata;
    private CartStatus cartStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;    
    
    private Money totalDiscountAmount;
    private Money totalPreDiscountPrice;    
    private Money totalDiscountedPrice;    
    private Money totalTaxAmount; 
    private Money grandTotal;
    private Money deliveryFee;
    
    public static PricedCartDto from(Cart cart){
//        Compute prices
        List<ItemPrice<CartItem>> ipList = Optional.ofNullable(cart.getItems())
                .orElse(Collections.emptyList()) // if null, use empty list
                .stream()
                .map((i)->new ItemPrice<CartItem>(i, i.getProduct().getUnitPrice(), 0d, i.getQuantity(), 0.075d))
                .collect(Collectors.toList());
        PriceHelper<CartItem> ph = new PriceHelper<>(ipList);
        
//        build list of priced cart items
        List<PricedCartItemDto> pricedItems = Optional.ofNullable(cart.getItems())
                .orElse(Collections.emptyList()) // if null, use empty list
                .stream()
                .map((i)-> { 
                    ItemPrice<CartItem> ip = ph.getItem(i);
                    return PricedCartItemDto.from(i, ip);
                })
                .collect(Collectors.toList());
//        Build priced cart
        PricedCartDto pricedCartDto = PricedCartDto.builder()
                .id(cart.getId())
                .tag(cart.getTag())
                .transactionRef(cart.getTransactionRef())
                .storeId(cart.getStore().getId())
                .storeName(cart.getStore().getName())
                .items(pricedItems)
                .customerId(cart.getCustomerId())
                .createdBy(cart.getCreatedBy())
                .metadata(cart.getMetadata())
                .cartStatus(cart.getCartStatus())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .totalDiscountAmount(ph.getTotalDiscountAmount())
                .totalPreDiscountPrice(ph.getTotalPreDiscountAmount())
                .totalDiscountedPrice(ph.getTotalDiscountedPrice())
                .totalTaxAmount(ph.getTotalTaxAmount())
                .grandTotal(ph.getGrandTotal())
//                Delivery fee has not be taken care of put this into considerationat a later time
                .build();
        
        return pricedCartDto;
    }
}
