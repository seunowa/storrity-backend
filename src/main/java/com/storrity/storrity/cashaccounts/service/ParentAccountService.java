/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cashaccounts.service;

import com.storrity.storrity.cashaccounts.dto.ChangeParentAccountInstruction;
import com.storrity.storrity.cashaccounts.entity.CashAccount;

/**
 *
 * @author Seun Owa
 */
public interface ParentAccountService {
    
    public CashAccount changeParentAccount(ChangeParentAccountInstruction instruction);
}
