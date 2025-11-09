/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.dto;

import com.storrity.storrity.cashaccounts.entity.CashAccountStatus;
import com.storrity.storrity.cashaccounts.entity.Money;
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
public class CashAccountUpdateDto {
    private String name;
    private CashAccountStatus status;
    private Money minimumBalance;
    private Boolean enforceMinimumBalance;
    @Email(message = "Please provide a valid email address")
    private String email;
    private String phone;
}
