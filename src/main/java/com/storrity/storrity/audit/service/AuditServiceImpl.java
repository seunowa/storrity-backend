/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.audit.service;

import com.storrity.storrity.audit.dto.AuditCreationDto;
import com.storrity.storrity.audit.entity.Audit;
import com.storrity.storrity.audit.entity.AuditQueryParam;
import com.storrity.storrity.audit.event.AuditDeletedEvent;
import com.storrity.storrity.audit.repository.AuditRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
public class AuditServiceImpl implements AuditService{
    
    private final AuditRepository auditRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository, ApplicationEventPublisher eventPublisher) {
        this.auditRepository = auditRepository;
        this.eventPublisher = eventPublisher;
    }    

    @Override
    public Audit create(AuditCreationDto dto) {
        Audit newAudit = Audit.builder()
                .action(dto.getAction())
                .actionType(dto.getActionType())
                .metadata(dto.getMetadata())
                .note(dto.getNote())
                .performedBy(dto.getPerformedBy())
                .resourceId(dto.getResourceId())
                .sourceModule(dto.getSourceModule())
                .build();
        
        Audit savedAudit = auditRepository.save(newAudit);
        return savedAudit;
    }

    @Override
    public Audit fetch(UUID id) {
        return auditRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundAppException("Audit not found with id: " + id));
    
    }

    @Override
    public List<Audit> list(AuditQueryParam params) {
        return auditRepository.list(params);
    }

    @Override
    public CountDto count(AuditQueryParam params) {
        return CountDto
                .builder()
                .count(auditRepository.countRecords(params))
                .build();
    }

    @Override
    public Audit delete(UUID id) {
        Audit a = fetch(id);
        auditRepository.deleteById(id);
        eventPublisher.publishEvent(new AuditDeletedEvent(a));
        return a;
    }
    
}
