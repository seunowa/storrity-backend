/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.entity;

import com.storrity.storrity.cashaccounts.dto.Flow;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransactionStep {
    @NotBlank(message = "account id is required")
    private String accountId;
    @NotNull(message = "Flow is required")
    private Flow flow;
    @NotNull(message = "amount is required")
    @PositiveMoney(message = "amount must be greater then zero")
    private Money amount;
}
