/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.dto;

import com.storrity.storrity.cashaccounts.entity.AccountTransactionStep;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
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
public class AccountTransactionInstruction {
    private String transactionRef;
    private String performedBy;
    private String description; 
    private String tag;
    @Valid
    @Size(min = 1, message = "Steps must contain at least one item")
    private List<AccountTransactionStep> steps;
}
