/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.security.repository;

import com.storrity.storrity.security.entity.UserProfile;
import com.storrity.storrity.security.entity.UserProfileQueryParams;
import java.util.List;

/**
 *
 * @author Seun
 */
public interface UserProfileRepositoryCustom {    
    public List<UserProfile> list(UserProfileQueryParams params);
    public Long countRecords (UserProfileQueryParams params);
}
