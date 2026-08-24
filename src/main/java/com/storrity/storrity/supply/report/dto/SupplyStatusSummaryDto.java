/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.supply.entity.SupplyStatus;
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
public class SupplyStatusSummaryDto {
    private SupplyStatus supplyStatus;
    private Long supplyCount;
    private Money totalValue;

    public SupplyStatusSummaryDto(
            String supplyStatus,
            Long supplyCount,
            Long totalValue) {

        this.supplyStatus = supplyStatus == null ? null : SupplyStatus.valueOf(supplyStatus);
        this.supplyCount = supplyCount;
        this.totalValue = Money.ofMicroNaira(totalValue);
    }
}
