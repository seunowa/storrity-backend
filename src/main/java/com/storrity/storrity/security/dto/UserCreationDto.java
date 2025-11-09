/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 *
 * @author Seun Owa
 */
@Data
public class UserCreationDto {
    @Email
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String role;
}
