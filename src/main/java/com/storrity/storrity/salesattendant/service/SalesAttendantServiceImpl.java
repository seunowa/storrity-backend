/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.salesattendant.service;

import com.storrity.storrity.salesattendant.dto.SalesAttendantCreationDto;
import com.storrity.storrity.salesattendant.dto.SalesAttendantUpdateDto;
import com.storrity.storrity.salesattendant.entity.SalesAttendant;
import com.storrity.storrity.salesattendant.entity.SalesAttendantQueryParams;
import com.storrity.storrity.salesattendant.repository.SalesAttendantRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
public class SalesAttendantServiceImpl implements SalesAttendantService{
    
    private final SalesAttendantRepository salesAttendantRepository;

    public SalesAttendantServiceImpl(SalesAttendantRepository salesAttendantRepository) {
        this.salesAttendantRepository = salesAttendantRepository;
    }

    @Override
    public SalesAttendant create(SalesAttendantCreationDto dto) {
        SalesAttendant sa = SalesAttendant.builder()
                .salesWallet(dto.getSalesWallet())
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
        
        sa.setSalesWallet(dto.getSalesWallet());
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
