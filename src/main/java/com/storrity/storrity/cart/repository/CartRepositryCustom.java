/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cart.repository;

import com.storrity.storrity.cart.entity.Cart;
import com.storrity.storrity.cart.entity.CartQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface CartRepositryCustom {    
    public List<Cart> list(CartQueryParams params);
    public Long countRecords (CartQueryParams params);
}
