/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.customer.dto;

import com.storrity.storrity.cashaccounts.dto.CashAccountDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.UUID;

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
public class CustomerDto {
    private UUID id;
    private String fullName;
    private String phone;
    private String email;
    private String state;
    private String city;
    private String street;
    private CashAccountDto mainCashAccount;
    private CashAccountDto creditCashAccount;
    private CashAccountDto rewardCashAccount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
