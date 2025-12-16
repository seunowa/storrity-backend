/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.license.event;

import com.storrity.storrity.license.entity.ClientSystem;
import com.storrity.storrity.util.event.AppEvent;

/**
 *
 * @author Seun Owa
 */
public class ClientSystemCreatedEvent extends AppEvent<ClientSystem>{
    
    public ClientSystemCreatedEvent(ClientSystem data) {
        super(data);
    }
    
}
