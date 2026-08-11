/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Seun Owa
 *
 * Operating hours for a single day. A null openingTime/closingTime pair
 * (or isClosed = true) means the business is closed that day.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayHours {
//    @NotNull(message = "Day of week is required")
//    private DayOfWeek dayOfWeek;
    @Schema(
        description = "Opening time in 24-hour format.",
        example = "08:00"
    )
    private LocalTime openingTime;
    @Schema(
        description = "Closing time in 24-hour format.",
        example = "21:00"
    )
    private LocalTime closingTime;
    
    @Schema(
        description = "Whether the store is closed on this day.",
        example = "false"
    )
    private boolean closed;

    public boolean isOpenAt(LocalTime time) {
        if (closed || openingTime == null || closingTime == null) {
            return false;
        }
        return !time.isBefore(openingTime) && !time.isAfter(closingTime);
    }
    
    /**
     * Cross-field validation:
     * - If closed, opening and closing times are ignored/optional.
     * - If open, both opening and closing times are mandatory, and openingTime must be strictly before closingTime.
     */
    @AssertTrue(message = "When open, opening time and closing time are required, and opening time must be before closing time")
    @JsonIgnore
    public boolean isValidSchedule() {
        if (closed) {
            return true;
        }
        if (openingTime == null || closingTime == null) {
            return false;
        }
        return openingTime.isBefore(closingTime);
    }
}
