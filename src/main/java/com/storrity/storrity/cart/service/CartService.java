/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cart.service;

import com.storrity.storrity.cart.dto.CartCreationDto;
import com.storrity.storrity.cart.dto.CartDto;
import com.storrity.storrity.cart.dto.CartItemInsertDto;
import com.storrity.storrity.cart.dto.CartUpdateDto;
import com.storrity.storrity.cart.dto.PricedCartDto;
import com.storrity.storrity.cart.entity.CartQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface CartService {
    public PricedCartDto create(CartCreationDto dto);
    public PricedCartDto fetch(UUID id);
    public List<CartDto> list(CartQueryParams params);
    public CountDto count(CartQueryParams params);
    public PricedCartDto update(UUID id, CartUpdateDto dto);
    public PricedCartDto delete(UUID id);
    public PricedCartDto updateCartItems(UUID id, CartItemInsertDto dto);
    public PricedCartDto deleteCartItem(UUID id);
}
