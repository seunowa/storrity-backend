/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
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
public class SalesByBrandDto {
    private String brand;
    private Long transactions;
    private Double quantitySold;
    private Money grossSales;
    private Money discount;
    private Money tax;
    private Money netSales;

    public SalesByBrandDto(
            String brand,
            Long transactions,
            Double quantitySold,
            Long grossSales,
            Long discount,
            Long tax,
            Long netSales) {

        this.brand = brand;
        this.transactions = transactions;
        this.quantitySold = quantitySold;

        this.grossSales = Money.ofMicroNaira(grossSales);
        this.discount = Money.ofMicroNaira(discount);
        this.tax = Money.ofMicroNaira(tax);
        this.netSales = Money.ofMicroNaira(netSales);
    }
}
