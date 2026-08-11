/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Seun Owa
 * Weekly operating hours. Days not present in the map are treated as closed.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    description = """
        Weekly operating hours.
        
        The map keys must be valid DayOfWeek values:
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, or SUNDAY.
        """
)
public class OperatingHours {
    @NotNull(message = "Hours map must not be null")
    @Valid
    @Builder.Default
    @Schema(
        description = """
                Operating hours indexed by day of the week.

                Use MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY,
                SATURDAY or SUNDAY as the map keys.
                """,
        example = """
                {
                  "MONDAY": {
                    "openingTime": "08:00",
                    "closingTime": "21:00",
                    "closed": false
                  },
                  "TUESDAY": {
                    "openingTime": "08:00",
                    "closingTime": "21:00",
                    "closed": false
                  },
                  "WEDNESDAY": {
                    "openingTime": "08:00",
                    "closingTime": "21:00",
                    "closed": false
                  },
                  "THURSDAY": {
                    "openingTime": "08:00",
                    "closingTime": "21:00",
                    "closed": false
                  },
                  "FRIDAY": {
                    "openingTime": "08:00",
                    "closingTime": "22:00",
                    "closed": false
                  },
                  "SATURDAY": {
                    "openingTime": "09:00",
                    "closingTime": "18:00",
                    "closed": false
                  },
                  "SUNDAY": {
                    "closed": true
                  }
                }
                """
    )
    private Map<DayOfWeek, @NotNull DayHours> hours = new EnumMap<>(DayOfWeek.class);

    public DayHours forDay(DayOfWeek day) {
        return hours.get(day);
    }

    public boolean isOpenAt(DayOfWeek day, LocalTime time) {
        DayHours dayHours = hours.get(day);
        return dayHours != null && dayHours.isOpenAt(time);
    }

    /**
     * Human-readable summary for prompt injection, e.g.:
     * "Mon-Fri 08:00-21:00, Sat 09:00-18:00, Sun closed"
     */
    public String toPromptText() {

        List<DayOfWeek> orderedDays = List.of(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY);

        StringBuilder sb = new StringBuilder();

        for (DayOfWeek day : orderedDays) {
            DayHours dh = hours.get(day);
            String label = day.name().substring(0, 1)
                    + day.name().substring(1, 3).toLowerCase();

            if (dh == null || dh.isClosed()) {
                sb.append(label).append(" closed; ");
            } else {
                sb.append(label).append(" ")
                        .append(dh.getOpeningTime())
                        .append("-")
                        .append(dh.getClosingTime())
                        .append("; ");
            }
        }

        return sb.toString().trim();
    }
}
