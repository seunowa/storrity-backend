/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.audit.service;

import com.storrity.storrity.audit.dto.AuditCreationDto;
import com.storrity.storrity.audit.entity.Audit;
import com.storrity.storrity.audit.entity.AuditQueryParam;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface AuditService {
    public Audit create(AuditCreationDto dto);
    public Audit fetch(UUID id);
    public List<Audit> list(AuditQueryParam params);
    public CountDto count(AuditQueryParam params);
    public Audit delete(UUID id);
}
