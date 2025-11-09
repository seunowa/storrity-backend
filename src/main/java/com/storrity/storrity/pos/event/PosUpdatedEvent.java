/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.pos.event;

import com.storrity.storrity.pos.entity.Pos;
import com.storrity.storrity.util.event.AppUpdateEvent;

/**
 *
 * @author Seun Owa
 */
public class PosUpdatedEvent extends AppUpdateEvent<Pos>{
    
    public PosUpdatedEvent(Pos data, Pos prev) {
        super(data, prev);
    }
}
