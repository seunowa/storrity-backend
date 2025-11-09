/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cashaccounts.service;

import com.storrity.storrity.cashaccounts.dto.AccountTransactionInstruction;
import com.storrity.storrity.cashaccounts.dto.AccountValidationDto;
import com.storrity.storrity.cashaccounts.entity.AccountTransaction;
import com.storrity.storrity.cashaccounts.entity.AccountTransactionQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface AccountTransactionService {
    public AccountTransaction create(AccountTransactionInstruction instruction);
    public AccountTransaction fetch(UUID id);
    public List<AccountTransaction> list(AccountTransactionQueryParams params);
    public CountDto count(AccountTransactionQueryParams params);
}
