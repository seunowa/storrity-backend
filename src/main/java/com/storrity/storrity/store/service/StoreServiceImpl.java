/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.store.service;

import com.storrity.storrity.store.dto.StoreCreationDto;
import com.storrity.storrity.store.dto.StoreUpdateDto;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.entity.StoreQueryParams;
import com.storrity.storrity.store.event.StoreCreatedEvent;
import com.storrity.storrity.store.event.StoreDeletedEvent;
import com.storrity.storrity.store.event.StoreUpdatedEvent;
import com.storrity.storrity.store.repository.StoreRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class StoreServiceImpl implements StoreService{
    
    private final StoreRepository storeRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, ApplicationEventPublisher eventPublisher) {
        this.storeRepository = storeRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Store create(StoreCreationDto dto) {
        Store newStore = Store.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .state(dto.getState())
                .city(dto.getCity())
                .street(dto.getStreet())
                .managerName(dto.getManagerName())
                .managerPhone(dto.getManagerPhone())
                .managerEmail(dto.getManagerEmail())
                .managerAddress(dto.getManagerAddress())
                .status(dto.getStatus())
                .build();
        
        Store saved = storeRepository.save(newStore);
        
        eventPublisher.publishEvent(new StoreCreatedEvent(saved));
        return saved;
    }

    @Override
    public Store fetch(UUID id) {
        return storeRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Store not found with id: " + id));
    }

    @Override
    public List<Store> list(StoreQueryParams params) {
        return storeRepository.list(params);
    }

    @Override
    public CountDto count(StoreQueryParams params) {
        return CountDto
                .builder()
                .count(storeRepository.countRecords(params))
                .build();
    }

    @Override
    @Transactional
    public Store update(UUID id, StoreUpdateDto dto) {
        Store s = fetch(id);
        
        if(dto.getName() != null){
            s.setName(dto.getName());
        }
        
        if(dto.getPhone() != null){
            s.setPhone(dto.getPhone());
        }
        
        if(dto.getEmail() != null){
            s.setEmail(dto.getEmail());
        }
        
        if(dto.getState() != null){
            s.setState(dto.getState());
        }
        
        if(dto.getCity() != null){
            s.setCity(dto.getCity());
        }
        
        if(dto.getStreet() != null){
            s.setStreet(dto.getStreet());
        }
        
        if(dto.getManagerName() != null){
            s.setManagerName(dto.getManagerName());
        }
        
        if(dto.getManagerPhone() != null){
            s.setManagerPhone(dto.getManagerPhone());
        }
        
        if(dto.getManagerEmail() != null){
            s.setManagerEmail(dto.getManagerEmail());
        }
        
        if(dto.getManagerAddress() != null){
            s.setManagerAddress(dto.getManagerAddress());
        }
        
        if(dto.getStatus() != null){
            s.setStatus(dto.getStatus());
        }
        
        Store saved = storeRepository.save(s);        
        eventPublisher.publishEvent(new StoreUpdatedEvent(saved));        
        return saved;
    }

    @Override
    @Transactional
    public Store delete(UUID id) {
        Store s = fetch(id);
        storeRepository.deleteById(id);
        eventPublisher.publishEvent(new StoreDeletedEvent(s));
        return s;
    }
    
}
