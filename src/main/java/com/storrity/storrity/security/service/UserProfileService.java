/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.security.service;

import com.storrity.storrity.security.dto.UserProfileDto;
import com.storrity.storrity.security.dto.UserProfileUpdateDto;
import com.storrity.storrity.security.entity.UserProfileQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface UserProfileService {
    public UserProfileDto fetch(String username);
    public List<UserProfileDto> list(UserProfileQueryParams params);
    public CountDto count(UserProfileQueryParams params);
    public UserProfileDto update(String username, UserProfileUpdateDto dto);
}
