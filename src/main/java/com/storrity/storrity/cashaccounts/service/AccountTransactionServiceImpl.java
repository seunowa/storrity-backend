/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.service;

import com.storrity.storrity.cashaccounts.dto.AccountTransactionInstruction;
import com.storrity.storrity.cashaccounts.dto.AccountValidationDto;
import com.storrity.storrity.cashaccounts.entity.AccountTransactionStep;
import static com.storrity.storrity.cashaccounts.dto.Flow.CREDIT;
import static com.storrity.storrity.cashaccounts.dto.Flow.DEBIT;
import com.storrity.storrity.cashaccounts.entity.AccountTransaction;
import com.storrity.storrity.cashaccounts.entity.AccountTransactionQueryParams;
import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.entity.CashAccountStatus;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.cashaccounts.entity.StatementItem;
import com.storrity.storrity.cashaccounts.event.AccountTransactionCreatedEvent;
import com.storrity.storrity.cashaccounts.repository.AccountTransactionRepository;
import com.storrity.storrity.cashaccounts.repository.CashAccountRepository;
import com.storrity.storrity.cashaccounts.repository.StatementItemRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class AccountTransactionServiceImpl implements AccountTransactionService{
    
    private final AccountTransactionRepository  accountTransactionRepository;
    private final CashAccountRepository cashAccountRepository;
    private final StatementItemRepository statementItemRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public AccountTransactionServiceImpl(AccountTransactionRepository accountTransactionRepository
            , CashAccountRepository cashAccountRepository, StatementItemRepository statementItemRepository
            , ApplicationEventPublisher eventPublisher) {
        this.accountTransactionRepository = accountTransactionRepository;
        this.cashAccountRepository = cashAccountRepository;
        this.statementItemRepository = statementItemRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public AccountTransaction create(AccountTransactionInstruction instruction) { 
        
        // retrieve accounts in instuction items
        Set<String> accountIds = instruction.getSteps().stream()
                .map((ai)->ai.getAccountId())
                .collect(Collectors.toSet());
        List<CashAccount> accs = cashAccountRepository.findAllByIdForUpdate(accountIds);
        // retrieve parent accounts of accounts in instruction items
        Set<String> parentAccIds = accs.stream()
            .filter(ai -> ai.getParentAccountId() != null)
            .map(ai -> ai.getParentAccountId())
            .collect(Collectors.toSet());
        List<CashAccount> parentAccs = cashAccountRepository.findAllByIdForUpdate(parentAccIds);
        // merge account in instruction and parent accounts together
        Set<CashAccount> accts = new LinkedHashSet<>(accs);
        accts.addAll(parentAccs);
        
        // Create a map of account ID to CashAccount
        Map<String, CashAccount> accountMap = accts.stream()
            .collect(Collectors.toMap(CashAccount::getId, Function.identity()));
        
        //validate accounts are active and create parent steps by adding steps to credit/debit parent account where applicable
        List<String> inActiveAccountIds = new ArrayList<>();
        Set<AccountTransactionStep> enrichedSteps = new LinkedHashSet<>(instruction.getSteps());
        LocalDateTime now = LocalDateTime.now();
        for (AccountTransactionStep step : instruction.getSteps()) {            
            CashAccount account = accountMap.get(step.getAccountId());
            account.setLastTxnAt(now);
            if(account == null){
                throw new ResourceNotFoundAppException("Account not found with id: " + step.getAccountId());
            }
        
            if (CashAccountStatus.ACTIVE != account.getStatus()) {
                inActiveAccountIds.add(step.getAccountId());
            }            
            
            String parentAccountId = account.getParentAccountId();
            if( parentAccountId != null){
                CashAccount parentAccount = accountMap.get(parentAccountId);
                parentAccount.setLastTxnAt(now);
                if(account == null){
                    throw new ResourceNotFoundAppException("Parent account not found with id: " + step.getAccountId());
                }
                
                if (CashAccountStatus.ACTIVE != parentAccount.getStatus()) {
                    inActiveAccountIds.add(parentAccountId);
                }
                
                AccountTransactionStep parentAccountStep = AccountTransactionStep.builder()
                        .accountId(account.getParentAccountId())
                        .amount(step.getAmount())
                        .flow(step.getFlow())
                        .build();
                
                if (!enrichedSteps.contains(parentAccountStep)) {
                    enrichedSteps.add(parentAccountStep);
                }
                
            }
        }        
        
        if(!inActiveAccountIds.isEmpty()){
            throw new IllegalStateException("Inactive accounts: " + inActiveAccountIds);                
        }
        
        instruction.setSteps(new ArrayList<>(enrichedSteps));    
        List<StatementItem> statementItems = applyInstructionItems(instruction, accountMap);
       
        
        AccountTransaction at = AccountTransaction.from(instruction);
        AccountTransaction savedAt = accountTransactionRepository.save(at);
        cashAccountRepository.saveAll(accountMap.values());
        statementItemRepository.saveAll(statementItems);
        eventPublisher.publishEvent(new AccountTransactionCreatedEvent(savedAt));
        return savedAt;
    }
    
    private List<StatementItem> applyInstructionItems(AccountTransactionInstruction instruction,
            Map<String, CashAccount> accountMap){
        List<StatementItem> statementItems = new ArrayList<>();
        for(int i = 0; i < instruction.getSteps().size(); i++){
            AccountTransactionStep step = instruction.getSteps().get(i);
            CashAccount acc = accountMap.get(step.getAccountId());
            Money amount = step.getAmount();
            Integer transactionIndex = i+1;
            AccountMutationResult mutationResult;
            
            if (step.getFlow() == null) {
                throw new IllegalArgumentException("Flow cannot be null for step: " + step);
            }
            switch (step.getFlow()) {
                case CREDIT -> mutationResult = applyCredit(instruction, transactionIndex, acc, amount);
                case DEBIT -> mutationResult = applyDebit(instruction, transactionIndex, acc, amount);
                default -> throw new IllegalStateException("Unknown flow");
            }
            
            StatementItem si = mutationResult.getStatementItem();
            statementItems.add(si);
        }
        return statementItems;
    }

    @Override
    public AccountTransaction fetch(UUID id) {
        return accountTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundAppException("Account transaction not found with id: " + id));
    
    }

    @Override
    public List<AccountTransaction> list(AccountTransactionQueryParams params) {
        return accountTransactionRepository.list(params);
    }

    @Override
    public CountDto count(AccountTransactionQueryParams params) {
        return CountDto
                .builder()
                .count(accountTransactionRepository.countRecords(params))
                .build();
    }
    
    private AccountMutationResult applyCredit(AccountTransactionInstruction instruction
            , Integer transationIndex, CashAccount ca, Money amount){
        Money balance = ca.getBalance().add(amount);
        ca.setBalance(balance);
        
        StatementItem si = StatementItem.builder()
                .balance(balance)
                .cashAccountId(ca.getId())
                .cashAccountType(ca.getCashAccountType())
                .credit(amount)
                .debit(new Money(0))
                .description(instruction.getDescription())
                .transactionIndex(transationIndex)
                .transactionRef(instruction.getTransactionRef())
                .build();
        
        AccountMutationResult result = new AccountMutationResult(ca, si);
        return result;
    }
    
    private AccountMutationResult applyDebit(AccountTransactionInstruction instruction
            , Integer transationIndex, CashAccount ca, Money amount){
        Money balance = ca.getBalance().subtract(amount);
        // Validate sufficient balance
        if(ca.getEnforceMinimumBalance()){
            if(ca.getMinimumBalance() == null){
                throw new IllegalStateException("Minimum balance is enforced but not set for account: " + ca.getId());
            }
            
            if(balance.getValueInMicroNaira() < ca.getMinimumBalance().getValueInMicroNaira()){
                throw new IllegalStateException("Insufficient balance in account " + ca.getId() 
                        + ". Required: " + amount + ", Available: " + ca.getBalance());
            }
        }       
        
        ca.setBalance(balance);
        
        StatementItem si = StatementItem.builder()
                .balance(balance)
                .cashAccountId(ca.getId())
                .cashAccountType(ca.getCashAccountType())
                .credit(new Money(0))
                .debit(amount)
                .description(instruction.getDescription())
                .transactionIndex(transationIndex)
                .transactionRef(instruction.getTransactionRef())
                .build();
        
        AccountMutationResult result = new AccountMutationResult(ca, si);
        return result;
    }
}
