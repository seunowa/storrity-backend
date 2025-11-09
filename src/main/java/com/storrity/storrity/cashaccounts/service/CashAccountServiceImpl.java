/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.service;

import com.storrity.storrity.cashaccounts.dto.AccountValidationDto;
import com.storrity.storrity.cashaccounts.event.CashAccountCreatedEvent;
import com.storrity.storrity.cashaccounts.dto.CashAccountCreationDto;
import com.storrity.storrity.cashaccounts.dto.CashAccountUpdateDto;
import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.entity.CashAccountQueryParams;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.cashaccounts.event.CashAccountDeletedEvent;
import com.storrity.storrity.cashaccounts.event.CashAccountUpdatedEvent;
import com.storrity.storrity.cashaccounts.repository.CashAccountRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceAlreadyExistsAppException;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class CashAccountServiceImpl implements CashAccountService{
    
    private final CashAccountRepository cashAccountRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Random random;
    
    @Autowired
    public CashAccountServiceImpl(CashAccountRepository cashAccountRepository, ApplicationEventPublisher eventPublisher){
        this.cashAccountRepository = cashAccountRepository;
        this.eventPublisher = eventPublisher;
        this.random = new Random();
    }
    
    private String generateAccountId(){
        return String.valueOf(random.nextLong(99999) + random.nextLong(99999));
    }

    @Override
    @Transactional
    public CashAccount create(CashAccountCreationDto dto) {
        
        Boolean isAccIdProvided = dto.getCashAccountId() != null && !dto.getCashAccountId().isEmpty();
        Boolean isParentAccIdProvided = dto.getParentAccountId()!= null && !dto.getParentAccountId().isEmpty();
        
        //check if account Id exists
        if(isAccIdProvided){            
            Optional<CashAccount> caOptional = cashAccountRepository.findById(dto.getCashAccountId());
            
            if(caOptional.isPresent()){
                throw new ResourceAlreadyExistsAppException("Account already exists with id: " + dto.getCashAccountId());
            }
        }       
        
        //check if parane accunt exists
        if(isParentAccIdProvided){
            fetch(dto.getParentAccountId());
        }
        
        //generate account id if account id is not provided
        String accountId = (isAccIdProvided)
                ? dto.getCashAccountId() : generateAccountId();        
        
        CashAccount newCa = CashAccount.builder()
                .id(accountId)
                .name(dto.getName())
                .balance(new Money(0))
                .minimumBalance(dto.getMinimumBalance())
                .enforceMinimumBalance(dto.getEnforceMinimumBalance())
                .cashAccountType(dto.getCashAccountType())
                .parentAccountId(dto.getParentAccountId())
                .status(dto.getStatus())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
        
        CashAccount saved  = cashAccountRepository.save(newCa);
        
        eventPublisher.publishEvent(new CashAccountCreatedEvent(saved ));        
        return saved;
    }

    @Override
    public CashAccount fetch(String id) {
        return cashAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundAppException("Account not found with id: " + id));
    }

    @Override
    public CountDto count(CashAccountQueryParams params) {
        return CountDto
                .builder()
                .count(cashAccountRepository.countRecords(params))
                .build();
    }

    @Override
    public List<CashAccount> list(CashAccountQueryParams params) {
        return cashAccountRepository.list(params);
    }

    @Override
    @Transactional
    public CashAccount update(String id, CashAccountUpdateDto dto) {

        CashAccount ca = fetch(id);

        if (dto.getName() != null) {
            ca.setName(dto.getName());
        }

        if (dto.getStatus() != null) {
            ca.setStatus(dto.getStatus());
        }

        if (dto.getMinimumBalance() != null) {
            ca.setMinimumBalance(dto.getMinimumBalance());
        }

        if (dto.getEnforceMinimumBalance() != null) {
            ca.setEnforceMinimumBalance(dto.getEnforceMinimumBalance());
        }
        
        if(dto.getEmail() != null){
            ca.setEmail(dto.getEmail());
        }
        
        if(dto.getPhone() != null){
            ca.setPhone(dto.getPhone());
        }

        CashAccount saved = cashAccountRepository.save(ca);
        
        eventPublisher.publishEvent(new CashAccountUpdatedEvent(saved, ca));
        return saved;
    }

    @Override
    @Transactional
    public CashAccount delete(String id) {
        CashAccount ca = fetch(id);
        cashAccountRepository.deleteById(id);
        eventPublisher.publishEvent(new CashAccountDeletedEvent(ca));
        return ca;
    }
    
    @Override
    public AccountValidationDto validateAccount(String id) {
        CashAccount ca = cashAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundAppException("Account not found with id: " + id));
        
        AccountValidationDto result = AccountValidationDto.builder().name(ca.getName()).build();
        return result;
    }
    
}
