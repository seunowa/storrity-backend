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
public class WeeklySalesSummaryDto {
    private LocalDate reportingWeekStartDate;
    private Integer reportingYear;
    private Integer reportingWeek;
    private Long transactions;
    private Double quantitySold;
    private Money grossSales;
    private Money discount;
    private Money tax;
    private Money netSales;

    public WeeklySalesSummaryDto(
            java.sql.Date date,
            Integer reportingYear,
            Integer reportingWeek,
            Long transactions,
            Double quantitySold,
            Long grossSales,
            Long discount,
            Long tax,
            Long netSales) {

        this.reportingWeekStartDate = date.toLocalDate();
        this.reportingYear = reportingYear;
        this.reportingWeek = reportingWeek;
        this.transactions = transactions;
        this.quantitySold = quantitySold;

        this.grossSales = Money.ofMicroNaira(grossSales);
        this.discount = Money.ofMicroNaira(discount);
        this.tax = Money.ofMicroNaira(tax);
        this.netSales = Money.ofMicroNaira(netSales);
    }
}
