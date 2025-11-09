/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

import com.storrity.storrity.security.entity.UserPermission;
import io.jsonwebtoken.lang.Collections;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
public class UserPermissionsServiceImpl implements UserPermissionsService{

    @Override
    public List<UserPermission> list() {        
        return new ArrayList<>(Collections.arrayToList(UserPermission.values()));
    }
    
}
