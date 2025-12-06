/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.customer.service;

import com.storrity.storrity.cashaccounts.dto.CashAccountCreationDto;
import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.entity.CashAccountStatus;
import com.storrity.storrity.cashaccounts.entity.CashAccountType;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.cashaccounts.service.CashAccountService;
import com.storrity.storrity.customer.dto.CustomerCreationDto;
import com.storrity.storrity.customer.dto.CustomerUpdateDto;
import com.storrity.storrity.customer.entity.Customer;
import com.storrity.storrity.customer.entity.CustomerQueryParams;
import com.storrity.storrity.customer.repository.CustomerRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class CustomerServiceImpl implements CustomerService{
    
    private final CustomerRepository customerRepository;
    private final CashAccountService cashAccountService;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CashAccountService cashAccountService) {
        this.customerRepository = customerRepository;
        this.cashAccountService = cashAccountService;
    }
    
    @Transactional
    @Override
    public Customer create(CustomerCreationDto dto) {
        
        Customer customer = Customer.builder()
                .city(dto.getCity())
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .state(dto.getState())
                .street(dto.getStreet())
                .build();
        
//        @Todo customer cash accounts here
       if(dto.getPhone() != null){
           

       }
       
       //        create main cash account
            CashAccount mainCashAccount = 
                    cashAccountService.create(CashAccountCreationDto
                        .builder()
//                        .cashAccountId(dto.getPhone())
                        .cashAccountType(CashAccountType.MAIN)
                        .email(dto.getEmail())
                        .enforceMinimumBalance(true)
                        .minimumBalance(new Money(0))
                        .name(dto.getFullName())
                        .phone(dto.getPhone())
                        .status(CashAccountStatus.ACTIVE)
                        .build());
           
//        create credit cash account
            CashAccount creditCashAccount = 
                    cashAccountService.create(CashAccountCreationDto
                        .builder()
//                        .cashAccountId(dto.getPhone())
                        .cashAccountType(CashAccountType.CREDIT)
                        .email(dto.getEmail())
                        .enforceMinimumBalance(true)
                        .minimumBalance(new Money(0))
                        .name(dto.getFullName())
                        .phone(dto.getPhone())
                        .status(CashAccountStatus.ACTIVE)
                        .build());
           
//        create reward cash account
            CashAccount rewardCashAccount = 
                    cashAccountService.create(CashAccountCreationDto
                        .builder()
//                        .cashAccountId(dto.getPhone())
                        .cashAccountType(CashAccountType.REWARD)
                        .email(dto.getEmail())
                        .enforceMinimumBalance(true)
                        .minimumBalance(new Money(0))
                        .name(dto.getFullName())
                        .phone(dto.getPhone())
                        .status(CashAccountStatus.ACTIVE)
                        .build());
           
           
           customer.setMainCashAccountId(mainCashAccount.getId());
           customer.setCreditCashAccounId(creditCashAccount.getId());
           customer.setRewardCashAccountId(rewardCashAccount.getId());

        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public Customer fetch(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Customer not found with id: " + id)); 
    }

    @Override
    public List<Customer> list(CustomerQueryParams params) {
        return customerRepository.list(params);
    }

    @Override
    public CountDto count(CustomerQueryParams params) {
        return CountDto
                .builder()
                .count(customerRepository.countRecords(params))
                .build();
    }

    @Transactional
    @Override
    public Customer update(UUID id, CustomerUpdateDto dto) {
        Customer c = customerRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Customer not found with id: " + id));
        if(dto.getFullName() != null){
            c.setFullName(dto.getFullName());
        }
        
        if(dto.getPhone()!= null){
            c.setPhone(dto.getPhone());
        }
        
        if(dto.getEmail()!= null){
            c.setEmail(dto.getEmail());
        }
        
        if(dto.getFullName() != null){
            c.setFullName(dto.getFullName());
        }
        
        if(dto.getState()!= null){
            c.setState(dto.getState());
        }
        
        if(dto.getCity()!= null){
            c.setCity(dto.getCity());
        }
        
        if(dto.getStreet()!= null){
            c.setStreet(dto.getStreet());
        }
        
        Customer savedCustomer = customerRepository.save(c);
        return savedCustomer;
    }

    @Transactional
    @Override
    public Customer delete(UUID id) {
        Customer c = customerRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Customer not found with id: " + id));
        customerRepository.delete(c);
//        eventPublisher.publishEvent(new ProductDeletedEvent(p));
        return c;
    }
    
}
