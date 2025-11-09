/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.product.event;

import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.util.event.AppEvent;

/**
 *
 * @author Seun Owa
 */
public class ProductCreatedEvent extends AppEvent<Product>{
    
    public ProductCreatedEvent(Product data) {
        super(data);
    }
}
