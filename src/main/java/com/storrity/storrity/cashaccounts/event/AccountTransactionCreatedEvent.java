/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.event;

import com.storrity.storrity.cashaccounts.entity.AccountTransaction;
import com.storrity.storrity.util.event.AppEvent;

/**
 *
 * @author Seun Owa
 */
public class AccountTransactionCreatedEvent extends AppEvent<AccountTransaction>{
    public AccountTransactionCreatedEvent(AccountTransaction data) {
        super(data);
    }
}
