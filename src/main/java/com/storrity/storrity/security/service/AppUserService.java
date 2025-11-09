/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.security.service;

import com.storrity.storrity.security.dto.PasswordChangeDto;
import com.storrity.storrity.security.dto.PasswordRestDto;
import com.storrity.storrity.security.dto.RoleChangeDto;
import com.storrity.storrity.security.dto.UserCreationDto;
import com.storrity.storrity.security.dto.UserDetailDto;
import com.storrity.storrity.security.entity.AppUserQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface AppUserService {
    public UserDetailDto create(UserCreationDto dto);
    public UserDetailDto changePassword(PasswordChangeDto dto);
    public UserDetailDto resetPassword(PasswordRestDto dto);
    public UserDetailDto changeRole(RoleChangeDto dto);    
    public UserDetailDto fetch(String username);
    public List<UserDetailDto> list(AppUserQueryParams params);
    public CountDto count(AppUserQueryParams params);
    public UserDetailDto delete(String username);
}
