/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stockmovement.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class YearlyStockMovementSummaryDto {

    private int year;
    private long movements;
    private double quantityIn;
    private double quantityOut;
    private double netQuantity;
    private Money valueIn;
    private Money valueOut;
    private Money netValue;
    private String baseUnit;

    public YearlyStockMovementSummaryDto(
            int year,
            long movements,
            double quantityIn,
            double quantityOut,
            double netQuantity,
            long valueIn,
            long valueOut,
            long netValue,
            String baseUnit) {
        this.year = year;
        this.movements = movements;
        this.quantityIn = quantityIn;
        this.quantityOut = quantityOut;
        this.netQuantity = netQuantity;
        this.valueIn = Money.ofMicroNaira(valueIn);
        this.valueOut = Money.ofMicroNaira(valueOut);
        this.netValue = Money.ofMicroNaira(netValue);
        this.baseUnit = baseUnit;
    }
}