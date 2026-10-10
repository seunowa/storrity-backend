/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.salesattendant.service;

import com.storrity.storrity.cashaccounts.dto.CashAccountCreationDto;
import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.entity.CashAccountStatus;
import com.storrity.storrity.cashaccounts.entity.CashAccountType;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.cashaccounts.service.CashAccountService;
import com.storrity.storrity.salesattendant.dto.SalesAttendantCreationDto;
import com.storrity.storrity.salesattendant.dto.SalesAttendantUpdateDto;
import com.storrity.storrity.salesattendant.entity.SalesAttendant;
import com.storrity.storrity.salesattendant.entity.SalesAttendantQueryParams;
import com.storrity.storrity.salesattendant.repository.SalesAttendantRepository;
import com.storrity.storrity.security.dto.UserDetailDto;
import com.storrity.storrity.security.service.AppUserService;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceAlreadyExistsAppException;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
@Transactional
public class SalesAttendantServiceImpl implements SalesAttendantService{
    
    private final SalesAttendantRepository salesAttendantRepository;
    private final CashAccountService cashAccountService;
    private final AppUserService appUserService;

    public SalesAttendantServiceImpl(SalesAttendantRepository salesAttendantRepository,
            CashAccountService cashAccountService,
            AppUserService appUserService) {
        this.salesAttendantRepository = salesAttendantRepository;
        this.cashAccountService = cashAccountService;
        this.appUserService = appUserService;
    }

    @Override
    public SalesAttendant create(SalesAttendantCreationDto dto) {
        
        if(salesAttendantRepository.existsByUsername(dto.getUsername())){
            throw new ResourceAlreadyExistsAppException("Sales attendant records already exixts for user : " + dto.getUsername());
        }
        
        UserDetailDto userDetail = appUserService.fetch(dto.getUsername());
        
        CashAccount salesAccount = 
                    cashAccountService.create(CashAccountCreationDto
                        .builder()
//                        .cashAccountId(dto.getPhone())
                        .cashAccountType(CashAccountType.OPERATIONAL)
                        .email(userDetail.getUsername())
                        .enforceMinimumBalance(true)
                        .minimumBalance(new Money(0))
                        .name("SALES ACCOUNT - " + userDetail.getUsername())
//                        .phone(dto.getPhone())
                        .status(CashAccountStatus.ACTIVE)
                        .build());
        
        
        SalesAttendant sa = SalesAttendant.builder()
                .salesAccount(salesAccount.getId())
                .username(dto.getUsername())
                .build();
        
        SalesAttendant savedSa = salesAttendantRepository.save(sa);
        return savedSa;
    }

    @Override
    public SalesAttendant fetch(UUID id) {
        return salesAttendantRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Sales attendant not found with id: " + id));       
       
    }

    @Override
    public SalesAttendant fetchByUsername(String username) {
        return salesAttendantRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundAppException("Sales attendant not found with username: " + username));       
       
    }

    @Override
    public List<SalesAttendant> list(SalesAttendantQueryParams params) {
        return salesAttendantRepository.list(params);
    }

    @Override
    public CountDto count(SalesAttendantQueryParams params) {
        return  CountDto
                .builder()
                .count(salesAttendantRepository.countRecords(params))
                .build();
    }

    @Override
    public SalesAttendant update(UUID id, SalesAttendantUpdateDto dto) {
        SalesAttendant sa = salesAttendantRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Sales attendant not found with id: " + id));
        
        sa.setSalesAccount(dto.getSalesAccount());
        SalesAttendant salesSa = salesAttendantRepository.save(sa);
        return salesSa;
    }

    @Override
    public SalesAttendant delete(UUID id) {
        SalesAttendant sa = salesAttendantRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Sales attendant not found with id: " + id));
        salesAttendantRepository.delete(sa);
        return sa;
    }
    
}
