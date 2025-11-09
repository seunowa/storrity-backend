/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.event;

import com.storrity.storrity.supply.entity.Supply;
import com.storrity.storrity.util.event.AppUpdateEvent;

/**
 *
 * @author Seun Owa
 */
public class SupplyUpdatedEvent extends AppUpdateEvent<Supply>{
    
    public SupplyUpdatedEvent(Supply data, Supply prev) {
        super(data, prev);
    }
}
