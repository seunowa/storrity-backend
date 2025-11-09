/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.dto;

import com.storrity.storrity.security.entity.UserProfile;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class UserProfileDto {
    private UUID id;
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String state;
    private String city;
    private String street;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static UserProfileDto from(UserProfile userProfile){
        return UserProfileDto.builder()
                .id(userProfile.getId())
                .username(userProfile.getUsername())
                .fullName(userProfile.getFullName())
                .phone(userProfile.getPhone())
                .email(userProfile.getEmail())
                .state(userProfile.getState())
                .city(userProfile.getCity())
                .street(userProfile.getStreet())
                .createdAt(userProfile.getCreatedAt())
                .updatedAt(userProfile.getUpdatedAt())
                .build();
    }
}
