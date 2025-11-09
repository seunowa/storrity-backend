/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.dto;

import com.storrity.storrity.security.entity.AppUser;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

/**
 *
 * @author Seun Owa
 */
@Data
@Schema(description = "User response object")
public class UserDetailDto {
    private UUID id;
    private String username;
    private Boolean pwdChangeRequired;
    private LocalDateTime pwdUpdatedAt;
    private String role;

    public static UserDetailDto from(AppUser user) {
        UserDetailDto dto = new UserDetailDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPwdChangeRequired(user.getPwdChangeRequired());
        dto.setPwdUpdatedAt(user.getPwdUpdatedAt());
        dto.setRole(user.getRole().getId());
        return dto;
    }
}
