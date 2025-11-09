/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.security.repository;

import com.storrity.storrity.security.entity.UserRole;
import com.storrity.storrity.security.entity.UserRoleQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface UserRoleRepositoryCustom {       
    public List<UserRole> list(UserRoleQueryParams params);
    public Long countRecords (UserRoleQueryParams params);
}
