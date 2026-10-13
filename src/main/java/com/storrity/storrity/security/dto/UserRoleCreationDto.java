/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author Seun Owa
 */
@Data
public class UserRoleCreationDto {
    @NotBlank
    private String role;
    private Set<String> permissions;
}
