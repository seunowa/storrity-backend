/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.store.dto;

import com.storrity.storrity.store.entity.StoreStatus;
import jakarta.validation.constraints.Email;
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
public class StoreUpdateDto {    
    private String name;
    private String phone;
    @Email
    private String email;
    private String state;
    private String city;
    private String street;
    private String managerName;
    private String managerPhone;
    @Email
    private String managerEmail;
    private String managerAddress;
    private StoreStatus status;
}