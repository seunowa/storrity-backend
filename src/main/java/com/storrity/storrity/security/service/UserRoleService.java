/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.security.service;

import com.storrity.storrity.security.dto.UserRoleCreationDto;
import com.storrity.storrity.security.dto.UserRoleDto;
import com.storrity.storrity.security.dto.UserRoleUpdateDto;
import com.storrity.storrity.security.entity.UserRole;
import com.storrity.storrity.security.entity.UserRoleQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface UserRoleService {
    public UserRoleDto create(UserRoleCreationDto dto);
    public UserRole fetchRaw(String role);
    public UserRoleDto fetch(String role);
    public List<UserRoleDto> list(UserRoleQueryParams params);
    public CountDto count(UserRoleQueryParams params);
    public UserRoleDto update(String role, UserRoleUpdateDto dto);
    public UserRoleDto delete(String role);
}
