/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import java.time.LocalDate;
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
public class HourlySalesSummaryDto {

    private Integer hour;
    private LocalDate date;
    private Long transactions;
    private Double quantitySold;
    private Money grossSales;
    private Money discount;
    private Money tax;
    private Money netSales;

    public HourlySalesSummaryDto(
            Integer hour,
            java.sql.Date date,
//            LocalDate date,
            Long transactions,
            Double quantitySold,
            Long grossSales,
            Long discount,
            Long tax,
            Long netSales) {

        this.hour = hour;
        this.date = date.toLocalDate();
        this.transactions = transactions;
        this.quantitySold = quantitySold;

        this.grossSales = Money.ofMicroNaira(grossSales);
        this.discount = Money.ofMicroNaira(discount);
        this.tax = Money.ofMicroNaira(tax);
        this.netSales = Money.ofMicroNaira(netSales);
    }
}
