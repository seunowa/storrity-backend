/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cart.repository;

import com.storrity.storrity.cart.entity.Checkout;
import com.storrity.storrity.cart.entity.CheckoutQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface CheckoutRepositoryCustom {     
    public List<Checkout> list(CheckoutQueryParams params);
    public Long countRecords (CheckoutQueryParams params);
}
