/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.license.event;

import com.storrity.storrity.license.entity.ClientSystem;
import com.storrity.storrity.util.event.AppUpdateEvent;

/**
 *
 * @author Seun Owa
 */
public class ClientSystemUpdateEvent extends AppUpdateEvent<ClientSystem>{
    
    public ClientSystemUpdateEvent(ClientSystem data, ClientSystem prev) {
        super(data, prev);
    }
    
}
