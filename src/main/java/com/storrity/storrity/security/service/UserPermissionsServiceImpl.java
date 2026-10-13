/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

import com.storrity.storrity.security.entity.UserPermission;
import com.storrity.storrity.security.repository.UserPermissionRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
public class UserPermissionsServiceImpl implements UserPermissionsService{

//    @Override
//    public List<UserPermission> list() {        
//        return new ArrayList<>(Collections.arrayToList(UserPermission.values()));
//    }
    
    private final UserPermissionRepository repository;
    
    public UserPermissionsServiceImpl(UserPermissionRepository repository) {
        this.repository = repository; }
    
    @Override
    public List<UserPermission> list() {
        return repository.findAll(Sort.by("id"));
    }
    
}
