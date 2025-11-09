/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cashaccounts.repository;

import com.storrity.storrity.cashaccounts.entity.AccountTransaction;
import com.storrity.storrity.cashaccounts.entity.AccountTransactionQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface AccountTransactionRepositoryCustom {    
    public List<AccountTransaction> list(AccountTransactionQueryParams params);
    public Long countRecords(AccountTransactionQueryParams param);
}
