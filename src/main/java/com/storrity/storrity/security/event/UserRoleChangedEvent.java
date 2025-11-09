/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.event;

import com.storrity.storrity.security.dto.UserDetailDto;
import com.storrity.storrity.util.event.AppUpdateEvent;

/**
 *
 * @author Seun Owa
 */
public class UserRoleChangedEvent extends AppUpdateEvent<UserDetailDto>{
    
    public UserRoleChangedEvent(UserDetailDto data, UserDetailDto prev) {
        super(data, prev);
    }
}
