/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.report.dto;

import com.storrity.storrity.supply.entity.SupplyStatus;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplyReportQueryParams {
    private List<UUID> storeIds;
    private List<String> supplierIds;
    private List<UUID> productIds;
    private List<String> productCategories;
    private List<SupplyStatus> supplyStatuses;

    /**
     * Which reporting-date family the range below is applied to.
     * Defaults to EXPECTED_SUPPLY when omitted.
     */
    private SupplyReportDateField dateField;

    @Size(min = 2, max = 2, message = "dateRange must contain exactly two dates")
    private List<LocalDate> dateRange;

    private Integer offset;
    private Integer limit;

    public enum SupplyReportDateField {
        EXPECTED_SUPPLY,
        DRAFT_SUBMITTED,
        DRAFT_APPROVED,
        DELIVERED,
        DELIVERY_SUBMITTED,
        DELIVERY_APPROVED,
        RECEIVED,
        CANCELED
    }
}
