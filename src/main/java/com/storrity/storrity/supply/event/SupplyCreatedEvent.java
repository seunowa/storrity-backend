/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.event;

import com.storrity.storrity.supply.entity.Supply;
import com.storrity.storrity.util.event.AppEvent;

/**
 *
 * @author Seun Owa
 */
public class SupplyCreatedEvent extends AppEvent<Supply>{
    
    public SupplyCreatedEvent(Supply data) {
        super(data);
    }
}
