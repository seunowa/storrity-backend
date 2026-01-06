/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.license.service;

import com.storrity.storrity.license.dto.ClientSystemCreationDto;
import com.storrity.storrity.license.dto.ClientSystemUpdateDto;
import com.storrity.storrity.license.dto.IsAddClientSystemAllowedDto;
import com.storrity.storrity.license.entity.ClientSystem;
import com.storrity.storrity.license.entity.ClientSystemStatus;
import com.storrity.storrity.license.event.ClientSystemCreatedEvent;
import com.storrity.storrity.license.event.ClientSystemDeleteEvent;
import com.storrity.storrity.license.event.ClientSystemUpdateEvent;
import com.storrity.storrity.license.repository.ClientSystemQueryParams;
import com.storrity.storrity.license.repository.ClientSystemRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.BadRequestAppException;
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
public class ClientSystemServiceImpl implements ClientSystemService{
    
    private final ClientSystemRepository clientSystemRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final LicenseService licenseService;

    @Autowired
    public ClientSystemServiceImpl(ClientSystemRepository clientSystemRepository
            , ApplicationEventPublisher eventPublisher, LicenseService licenseService) {
        this.clientSystemRepository = clientSystemRepository;
        this.eventPublisher = eventPublisher;
        this.licenseService = licenseService;
    }    

    @Transactional
    @Override
    public ClientSystem create(ClientSystemCreationDto dto) {
        
        Boolean isAddAllowed = licenseService.isAddClientSystemAllowed().getIsAddClientSystemAllowed();
        
        if(!isAddAllowed){
            throw new BadRequestAppException("Number of client systems in licence exceeded");
        }
        
        ClientSystem cs = ClientSystem
                .builder()
                .clientId(dto.getClientId())
                .name(dto.getName())
                .status(dto.getStatus()!=null ? dto.getStatus() : ClientSystemStatus.INACTIVE)
                .build();
        
        clientSystemRepository.save(cs);
        eventPublisher.publishEvent(new ClientSystemCreatedEvent(cs));
        return cs;
    }

    @Override
    public ClientSystem fetch(UUID id) {
        return clientSystemRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundAppException("Client system not found with id: " + id));
    }

    @Override
    public ClientSystem fetchByClientId(String clientId) {
        return clientSystemRepository.findByClientId(clientId)
                .orElseThrow(()-> new ResourceNotFoundAppException("Client system not found with clientId: " + clientId));
    }

    @Override
    public List<ClientSystem> list(ClientSystemQueryParams params) {
        return clientSystemRepository.list(params);
    }

    @Override
    public CountDto count(ClientSystemQueryParams params) {
        return CountDto
                .builder()
                .count(clientSystemRepository.countRecords(params))
                .build();
    }

    @Transactional
    @Override
    public ClientSystem update(UUID id, ClientSystemUpdateDto dto) {
        ClientSystem cs = clientSystemRepository.findByIdForUpdate(id)
                .orElseThrow(()-> new ResourceNotFoundAppException("Client system not found with id: " + id));
        ClientSystem beforeSave = cs.copy();
        
        if(dto.getName()!= null){
            cs.setName(dto.getName());
        }
        
        if(dto.getStatus() != null){
            cs.setStatus(dto.getStatus());
        }
        
        ClientSystem savedCs = clientSystemRepository.save(cs);
        eventPublisher.publishEvent(new ClientSystemUpdateEvent(savedCs, beforeSave));
        return savedCs;
    }

    @Transactional
    @Override
    public ClientSystem delete(UUID id) {        
        ClientSystem cs = fetch(id);
        clientSystemRepository.delete(cs);
        eventPublisher.publishEvent(new ClientSystemDeleteEvent(cs));
        return cs;
    }
    
}
