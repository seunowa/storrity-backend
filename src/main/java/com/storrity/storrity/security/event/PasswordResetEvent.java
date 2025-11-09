/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.event;

import com.storrity.storrity.security.dto.UserDetailDto;
import com.storrity.storrity.util.event.AppEvent;

/**
 *
 * @author Seun Owa
 */
public class PasswordResetEvent extends AppEvent<UserDetailDto>{
    
    public PasswordResetEvent(UserDetailDto data) {
        super(data);
    }
}
