/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.dto;

import com.storrity.storrity.security.entity.UserPermission;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author Seun Owa
 */
@Data
public class UserRoleCreationDto {
    private String role;
    private Set<UserPermission> permissions;
}
