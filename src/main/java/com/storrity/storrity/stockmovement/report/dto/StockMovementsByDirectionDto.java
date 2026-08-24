/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stockmovement.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.stockmovement.entity.StockMoevmentDirection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */

//@Getter
//@Setter
//@NoArgsConstructor
//@SuperBuilder
//public class StockMovementsByDirectionDto {
//
//    private StockMoevmentDirection direction;
//    private long movements;
//    private double quantity;
//    private Money value;
//    private String baseUnit;
//
//    public StockMovementsByDirectionDto(
//            StockMoevmentDirection direction,
//            long movements,
//            double quantity,
//            long value,
//            String baseUnit) {
//        this.direction = direction;
//        this.movements = movements;
//        this.quantity = quantity;
//        this.value = Money.ofMicroNaira(value);
//        this.baseUnit = baseUnit;
//    }
//}

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class StockMovementsByDirectionDto {

    private StockMoevmentDirection direction;

    private long movements;

    private double quantityIn;

    private double quantityOut;

    private double netQuantity;

    private Money valueIn;

    private Money valueOut;

    private Money netValue;

    private String baseUnit;

    public StockMovementsByDirectionDto(
            StockMoevmentDirection direction,
            long movements,
            double quantityIn,
            double quantityOut,
            double netQuantity,
            long valueIn,
            long valueOut,
            long netValue,
            String baseUnit) {

        this.direction = direction;
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