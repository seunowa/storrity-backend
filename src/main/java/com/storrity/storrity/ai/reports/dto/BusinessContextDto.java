/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.reports.dto;

import com.storrity.storrity.ai.settings.entity.BusinessContext;
import com.storrity.storrity.ai.settings.entity.OperatingHours;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Schema(description = "Business context response object")
public class BusinessContextDto {
    @Schema(
        description = "Unique identifier of the business context",
        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
    )
    private UUID id;
    
    @Schema(
        description = "Store associated with this business context",
        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
    )
    private UUID storeId;

    @Schema(
        description = "The long-term vision of the business",
        example = "Become the leading convenience store in the local market",
        maxLength = 2000
    )
    @Size(max = 2000)
    private String vision;

    @Schema(
        description = "Business goals and desired outcomes",
        example = "Increase net sales by 25% in 2026",
        maxLength = 2000
    )
    @Size(max = 2000)
    private String goals;          // e.g. "Increase net sales by 25% in 2026"

    @Schema(
        description = "Sales targets such as monthly or quarterly targets",
        example = "Monthly sales target of NGN 15,000,000",
        maxLength = 2000
    )
    @Size(max = 2000)
    private String salesTargets;          // e.g. monthly/quarterly targets

    @Schema(
        description = "Known business challenges",
        example = "High stockouts in Category X and low customer conversion",
        maxLength = 2000
    )
    @Size(max = 2000)
    private String challenges;         // e.g. "High stockouts in Category X, low conversion in Store Y"

    @Schema(
        description = "Strategic priorities for the business",
        example = "Focus on premium products and expand into new locations",
        maxLength = 2000
    )
    @Size(max = 2000)
    private String strategicPriorities;   // e.g. "Focus on premium products, expand in Lagos"

     @Schema(
        description = "Known operational or business issues",
        example = "Poor stock visibility during peak sales periods",
        maxLength = 2000
    )
    @Size(max = 2000)
    private String knownIssues;           // operational problems

     @Schema(
        description = "Additional free-form business context",
        example = "The business experiences its highest sales between 5 PM and 8 PM",
        maxLength = 000
    )
    @Size(max = 3000)
    private String additionalNotes;       // free-form context

    @Schema(description = "Weekly operating hours of the store")
    private OperatingHours operatingHours;
    
    @Schema(
        description = "Date and time when the context was created",
        example = "2026-08-10T09:30:00"
    )
    private LocalDateTime createdAt;
    
    @Schema(
        description = "Date and time when the context was last updated",
        example = "2026-08-10T10:45:00"
    )
    private LocalDateTime updatedAt; 
    
    public static BusinessContextDto from(BusinessContext ctx){
        return BusinessContextDto.builder()
                .id(ctx.getId())
                .storeId(ctx.getStoreId())
                .vision(ctx.getVision())
                .goals(ctx.getGoals())
                .salesTargets(ctx.getSalesTargets())
                .challenges(ctx.getChallenges())
                .strategicPriorities(ctx.getStrategicPriorities())
                .knownIssues(ctx.getKnownIssues())
                .additionalNotes(ctx.getAdditionalNotes())
                .operatingHours(ctx.getOperatingHours())
                .createdAt(ctx.getCreatedAt())
                .updatedAt(ctx.getUpdatedAt())
                .build();
    }
}
