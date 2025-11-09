/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.security.repository;

import com.storrity.storrity.security.entity.AppUser;
import com.storrity.storrity.security.entity.AppUserQueryParams;
import java.util.List;

/**
 *
 * @author Seun
 */
public interface UserRepositoryCustom {    
    public List<AppUser> list(AppUserQueryParams params);
    public Long countRecords (AppUserQueryParams params);
}
