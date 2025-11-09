/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.service;

import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.entity.StatementItem;

/**
 *
 * @author Seun Owa
 */
public class AccountMutationResult {
        private CashAccount cashAccount;
        private StatementItem statementItem;

        public CashAccount getCashAccount() {
            return cashAccount;
        }

        public AccountMutationResult(CashAccount cashAccount, StatementItem statementItem) {
            this.cashAccount = cashAccount;
            this.statementItem = statementItem;
        }

        public void setCashAccount(CashAccount cashAccount) {
            this.cashAccount = cashAccount;
        }

        public StatementItem getStatementItem() {
            return statementItem;
        }

        public void setStatementItem(StatementItem statementItem) {
            this.statementItem = statementItem;
        }        
    }
