/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.audit.repository;

import com.storrity.storrity.audit.entity.Audit;
import com.storrity.storrity.audit.entity.AuditQueryParam;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface AuditRepositoryCustom {    
    public List<Audit> list(AuditQueryParam params);
    public Long countRecords (AuditQueryParam params);
}
