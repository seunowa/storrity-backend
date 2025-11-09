/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cashaccounts.repository;

import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.entity.CashAccountQueryParams;
import java.util.List;


/**
 *
 * @author Seun Owa
 */
public interface CashAccountRepositoryCustom {
    public List<CashAccount> list(CashAccountQueryParams params);
    public Long countRecords(CashAccountQueryParams param);
}
