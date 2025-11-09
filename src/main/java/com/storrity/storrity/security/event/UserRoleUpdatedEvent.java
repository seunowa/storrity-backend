/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.event;

import com.storrity.storrity.security.entity.UserRole;
import com.storrity.storrity.util.event.AppUpdateEvent;

/**
 *
 * @author Seun Owa
 */
public class UserRoleUpdatedEvent extends AppUpdateEvent<UserRole>{
    
    public UserRoleUpdatedEvent(UserRole data, UserRole prev) {
        super(data, prev);
    }
}
