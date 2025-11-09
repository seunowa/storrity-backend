/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.pos.service;

import com.storrity.storrity.pos.dto.PosCreationDto;
import com.storrity.storrity.pos.dto.PosUpdateDto;
import com.storrity.storrity.pos.entity.Pos;
import com.storrity.storrity.pos.entity.PosQueryParam;
import com.storrity.storrity.pos.event.PosCreatedEvent;
import com.storrity.storrity.pos.event.PosDeletedEvent;
import com.storrity.storrity.pos.event.PosUpdatedEvent;
import com.storrity.storrity.pos.repository.PosRepository;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.service.StoreService;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class PosServiceImpl implements PosService{
    
    private final PosRepository posRepository;
    private final StoreService storeService;
    private final ApplicationEventPublisher eventPublisher;

    public PosServiceImpl(PosRepository posRepository, StoreService storeService, ApplicationEventPublisher eventPublisher) {
        this.posRepository = posRepository;
        this.storeService = storeService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public Pos create(PosCreationDto dto) {
        Store store = storeService.fetch(dto.getStoreId());
        
        Pos newPos = Pos.builder()
                .identifier(dto.getIdentifier())
                .name(dto.getName())
                .status(dto.getStatus())
                .store(store)
                .build();
        
        Pos savedPos  = posRepository.save(newPos);
        eventPublisher.publishEvent(new PosCreatedEvent(savedPos));
        return savedPos;
    }

    @Override
    public Pos fetch(UUID id) {
        return posRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Pos not found with id: " + id));
    }

    @Override
    public List<Pos> list(PosQueryParam params) {
        return posRepository.list(params);
    }

    @Override
    public CountDto count(PosQueryParam params) {
        return CountDto
                .builder()
                .count(posRepository.countRecords(params))
                .build();
    }

    @Transactional
    @Override
    public Pos update(UUID id, PosUpdateDto dto) {
        Pos p = fetch(id);
        Pos beforeSave = p.copy();
        
        if(dto.getIdentifier() != null){
            p.setIdentifier(dto.getIdentifier());
        }
        
        if(dto.getName()!= null){
            p.setName(dto.getName());
        }
        
        if(dto.getStatus()!= null){
            p.setStatus(dto.getStatus());
        }
        
        if(dto.getStoreId()!= null){
            Store store = storeService.fetch(dto.getStoreId());
            p.setStore(store);
        }
        
        Pos savedPos = posRepository.save(p);
        eventPublisher.publishEvent(new PosUpdatedEvent(savedPos, beforeSave));
        return savedPos;
    }

    @Transactional
    @Override
    public Pos delete(UUID id) {
        Pos p = fetch(id);
        posRepository.delete(p);
        eventPublisher.publishEvent(new PosDeletedEvent(p));
        return p;
    }
    
}
