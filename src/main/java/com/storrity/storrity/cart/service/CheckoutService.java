/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cart.service;

import com.storrity.storrity.cart.dto.CheckoutCreationDto;
import com.storrity.storrity.cart.entity.Checkout;
import com.storrity.storrity.cart.entity.CheckoutQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface CheckoutService {
    public Checkout create(CheckoutCreationDto dto);
    public Checkout fetch(UUID id);
    public List<Checkout> list(CheckoutQueryParams params);
    public CountDto count(CheckoutQueryParams params);
}
