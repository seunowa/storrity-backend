/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.dto;

import com.storrity.storrity.security.entity.AppUser;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
    private Set<String> roles;

    public static UserDetailDto from(AppUser user) {
        UserDetailDto dto = new UserDetailDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPwdChangeRequired(user.getPwdChangeRequired());
        dto.setPwdUpdatedAt(user.getPwdUpdatedAt());
        dto.setRoles(user.getRoles() == null ? Set.of() : user.getRoles().stream()
                .map(role -> role.getId()).collect(Collectors.toSet()));
        return dto;
    }
}
