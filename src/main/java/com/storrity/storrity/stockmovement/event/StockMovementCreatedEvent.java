/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.stockmovement.event;

import com.storrity.storrity.stockmovement.dto.StockMovementResult;
import com.storrity.storrity.util.event.AppEvent;

/**
 *
 * @author Seun Owa
 */
public class StockMovementCreatedEvent extends AppEvent<StockMovementResult>{
    
    public StockMovementCreatedEvent(StockMovementResult data) {
        super(data);
    }
}
