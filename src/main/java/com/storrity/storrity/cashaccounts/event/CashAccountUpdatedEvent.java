/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.event;

import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.util.event.AppUpdateEvent;

/**
 *
 * @author Seun Owa
 */
public class CashAccountUpdatedEvent extends AppUpdateEvent<CashAccount>{
    public CashAccountUpdatedEvent(CashAccount data, CashAccount prev) {
        super(data, prev);
    }
}
