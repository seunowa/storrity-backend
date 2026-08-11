/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.reports.dto;

import com.storrity.storrity.ai.settings.entity.OperatingHours;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
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
@Schema(
    description = "Request object used to create or update AI business context for a store"
)
public class BusinessContextCreationDto {
    @Schema(
        description = "The long-term vision of the business",
        example = "Become the leading convenience store in the local market",
        maxLength = 2000
    )
    private String vision;

    @Schema(
        description = "Business goals and desired outcomes",
        example = "Increase net sales by 25% in 2026",
        maxLength = 2000
    )
    private String goals;          // e.g. "Increase net sales by 25% in 2026"

    @Schema(
        description = "Sales targets, such as monthly or quarterly targets",
        example = "Monthly sales target of NGN 15,000,000 and quarterly target of NGN 45,000,000",
        maxLength = 2000
    )
    private String salesTargets;          // e.g. monthly/quarterly targets

    @Schema(
        description = "Known business challenges affecting the store",
        example = "High stockouts in Category X and low customer conversion during weekdays",
        maxLength = 2000
    )
    private String challenges;         // e.g. "High stockouts in Category X, low conversion in Store Y"

    @Schema(
        description = "Strategic priorities that should guide business decisions",
        example = "Focus on premium products, improve customer retention, and expand into new locations",
        maxLength = 2000
    )
    private String strategicPriorities;   // e.g. "Focus on premium products, expand in Lagos"

    @Schema(
        description = "Known operational or business issues",
        example = "Poor stock visibility during peak sales periods",
        maxLength = 2000
    )
    private String knownIssues;           // operational problems

    @Schema(
        description = "Additional free-form business context that may be useful for AI analysis and recommendations",
        example = "The store experiences its highest customer traffic between 5 PM and 8 PM",
        maxLength = 3000
    )
    private String additionalNotes;       // free-form context

    @Valid
    @Schema(
        description = """
            Weekly operating hours of the store.

            The hours map uses the days of the week as keys:
            MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY and SUNDAY.

            Days that are not present in the map are treated as closed.
            """
    )
    private OperatingHours operatingHours;
}
