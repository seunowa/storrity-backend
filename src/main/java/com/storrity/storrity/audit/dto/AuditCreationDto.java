/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.audit.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author Seun Owa
 */
@Data
public class AuditCreationDto {
    private String resourceId;
    @NotNull
    private String action;
    @NotNull
    private String actionType;
    @NotNull
    @Column(name = "source_module")
    private String sourceModule;  
    @NotNull
    private String performedBy;
    @Column(name = "note")
    private String note; 
    private Map<String, Object> metadata;
}
