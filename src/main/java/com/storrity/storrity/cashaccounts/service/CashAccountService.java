/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cashaccounts.service;

import com.storrity.storrity.cashaccounts.dto.AccountValidationDto;
import com.storrity.storrity.cashaccounts.dto.CashAccountCreationDto;
import com.storrity.storrity.cashaccounts.dto.CashAccountUpdateDto;
import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.entity.CashAccountQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface CashAccountService {
    public CashAccount create(CashAccountCreationDto dto);
    public CashAccount fetch(String id);
    public List<CashAccount> list(CashAccountQueryParams params);
    public CountDto count(CashAccountQueryParams params);
    public CashAccount update(String id, CashAccountUpdateDto dto);
    public CashAccount delete(String id);
    public AccountValidationDto validateAccount(String id);
}
