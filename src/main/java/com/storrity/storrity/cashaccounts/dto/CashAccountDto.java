/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.dto;

import com.storrity.storrity.cashaccounts.entity.CashAccountStatus;
import com.storrity.storrity.cashaccounts.entity.CashAccountType;
import com.storrity.storrity.cashaccounts.entity.Money;
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
public class CashAccountDto {
  private UUID id;
  private String name;
  private String cashAccountNumber;
  private CashAccountType cashAccountType;
  private Money balance;
  private CashAccountStatus status;
  private Money minimumBalance;
    private Boolean enforceMinimumBalance;
  private LocalDateTime lastTxnAt;
}
