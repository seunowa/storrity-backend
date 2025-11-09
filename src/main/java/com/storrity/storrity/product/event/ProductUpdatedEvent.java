/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.product.event;

import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.util.event.AppUpdateEvent;

/**
 *
 * @author Seun Owa
 */
public class ProductUpdatedEvent extends AppUpdateEvent<Product>{
    
    public ProductUpdatedEvent(Product data, Product prev) {
        super(data, prev);
    }
}
