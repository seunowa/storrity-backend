/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.service;

import com.storrity.storrity.cashaccounts.dto.AccountTransactionInstruction;
import com.storrity.storrity.cashaccounts.entity.AccountTransactionStep;
import com.storrity.storrity.cashaccounts.dto.ChangeParentAccountInstruction;
import com.storrity.storrity.cashaccounts.dto.Flow;
import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.cashaccounts.event.CashAccountUpdatedEvent;
import com.storrity.storrity.cashaccounts.repository.CashAccountRepository;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class ParentAccountServiceImpl implements ParentAccountService{

    private final AccountTransactionService  accountTransactionService;
    private final CashAccountRepository cashAccountRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ParentAccountServiceImpl(AccountTransactionService  accountTransactionService
            , CashAccountRepository cashAccountRepository, ApplicationEventPublisher eventPublisher) {
        this.accountTransactionService = accountTransactionService;
        this.cashAccountRepository = cashAccountRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public CashAccount changeParentAccount(ChangeParentAccountInstruction instruction) {
        CashAccount account = cashAccountRepository.findByIdForUpdate(instruction.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundAppException("Account not found with id: " + instruction.getAccountId()));
        
        String currentParentAccountId = account.getParentAccountId();
        Money amount = new Money( Math.abs(account.getBalance().getValueInMicroNaira()));
        
        if( (currentParentAccountId != null || instruction.getNewParentAccountId() != null) 
                && account.getBalance().getValueInMicroNaira() != 0){
            
            List<AccountTransactionStep> steps = new ArrayList<>();
            //if balance is possitive debit current parent account else credit current parent account
            if(account.getParentAccountId() != null ){
                steps.add(AccountTransactionStep.builder()
                        .accountId(currentParentAccountId)
                        .amount(amount)
                        .flow( account.getBalance().isPositiveValue() ? Flow.DEBIT : Flow.CREDIT)
                        .build());
            }
            
            //if balance is possitive credit new parent account else debit new parent account
            if(instruction.getNewParentAccountId() != null ){
                steps.add(AccountTransactionStep.builder()
                        .accountId(instruction.getNewParentAccountId())
                        .amount(amount)
                        .flow(account.getBalance().isPositiveValue() ? Flow.CREDIT : Flow.DEBIT)
                        .build());
            }
            
            AccountTransactionInstruction transferInstruction = AccountTransactionInstruction.builder()
                    .description("Set parent account")
                    .performedBy(instruction.getPerformedBy())
                    .tag("Set parent account")
                    .steps(steps)
                    .transactionRef(instruction.getTransactionRef())
                    .build();
            accountTransactionService.create(transferInstruction);
        }        
        account.setParentAccountId(instruction.getNewParentAccountId());
        CashAccount savedAccount = cashAccountRepository.save(account);
        eventPublisher.publishEvent(new CashAccountUpdatedEvent(savedAccount, account));
        return savedAccount;
    }
    
}
