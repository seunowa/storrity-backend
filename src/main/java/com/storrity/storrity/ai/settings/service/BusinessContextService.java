/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.service;

import com.storrity.storrity.ai.reports.dto.BusinessContextCreationDto;
import com.storrity.storrity.ai.reports.dto.BusinessContextDto;
import com.storrity.storrity.ai.settings.entity.BusinessContext;
import com.storrity.storrity.ai.settings.entity.OperatingHours;
import com.storrity.storrity.ai.settings.repository.BusinessContextRepository;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
@RequiredArgsConstructor
public class BusinessContextService {

    private final BusinessContextRepository repository;
    
    public BusinessContextDto getGlobal() {
        BusinessContext ctx = repository.findByStoreIdIsNull()
                .orElseThrow(() -> new ResourceNotFoundAppException("Global context not found"));
        return BusinessContextDto.from(ctx);
    }
    
    public BusinessContextDto getForStore(UUID storeId){
        BusinessContext ctx = repository.findByStoreId(storeId)
                .orElseThrow(() -> new ResourceNotFoundAppException("Business context for store not found"));
        
        return BusinessContextDto.from(ctx);
    }
    
    @Transactional
    public BusinessContextDto saveGlobalCtx(BusinessContextCreationDto dto) {
        
        BusinessContext context = repository.findByStoreIdIsNull()
                .orElseGet(() -> BusinessContext.builder().build());
        
        context.setAdditionalNotes(dto.getAdditionalNotes());
        context.setChallenges(dto.getChallenges());
        context.setGoals(dto.getGoals());
        context.setKnownIssues(dto.getKnownIssues());
        context.setSalesTargets(dto.getSalesTargets());
        context.setStrategicPriorities(dto.getStrategicPriorities());
        context.setVision(dto.getVision());
        context.setOperatingHours(dto.getOperatingHours());
        
        BusinessContext ctx = repository.save(context);
        return BusinessContextDto.from(ctx);
    }
    
    @Transactional
    public BusinessContextDto saveStoreCtx(UUID storeId, BusinessContextCreationDto dto) {
        
        BusinessContext context = repository.findByStoreId(storeId)
                .orElseGet(() -> BusinessContext.builder().storeId(storeId).build());
        
        
        context.setAdditionalNotes(dto.getAdditionalNotes());
        context.setChallenges(dto.getChallenges());
        context.setGoals(dto.getGoals());
        context.setKnownIssues(dto.getKnownIssues());
        context.setSalesTargets(dto.getSalesTargets());
        context.setStrategicPriorities(dto.getStrategicPriorities());
        context.setVision(dto.getVision());
        context.setOperatingHours(dto.getOperatingHours());

        BusinessContext ctx = repository.save(context);
        return BusinessContextDto.from(ctx);
    }
    
    public String getGlobalContextAsText(UUID storeId) {
        return getContextAsText(null);
    }

    public String getContextAsText(UUID storeId) {
        // 1. Try store-specific first
        Optional<BusinessContext> storeContext = Optional.empty();
        if (storeId != null) {
            storeContext = repository.findByStoreId(storeId);
        }

        // 2. Fallback to global
        BusinessContext context = storeContext
                .or(() -> repository.findByStoreIdIsNull())
                .orElse(null);

        if (context == null) {
            return "No business context has been configured.";
        }

        return format(context, storeId != null);
    }

    private String format(BusinessContext ctx, boolean isStoreSpecific) {
        String scope = isStoreSpecific ? "STORE-SPECIFIC" : "GLOBAL (Whole Business)";

        return """
                === BUSINESS CONTEXT (%s) ===
                Vision: %s
                Goals: %s
                Sales Targets: %s
                Challenges: %s
                Strategic Priorities: %s
                Known Issues: %s
                Operating Hours: %s
                Additional Notes: %s
                ================================
                """.formatted(
                scope,
                nullSafe(ctx.getVision()),
                nullSafe(ctx.getGoals()),
                nullSafe(ctx.getSalesTargets()),
                nullSafe(ctx.getChallenges()),
                nullSafe(ctx.getStrategicPriorities()),
                nullSafe(ctx.getKnownIssues()),
                nullSafeOperatingHours(ctx.getOperatingHours()),
                nullSafe(ctx.getAdditionalNotes())
        );
    }

    private String nullSafe(String value) {
        return value == null || value.isBlank() ? "Not specified" : value;
    }

    private String nullSafeOperatingHours(OperatingHours operatingHours) {
        if (operatingHours == null) {
            return "Not specified";
        }
        return operatingHours.toPromptText();
    }
}