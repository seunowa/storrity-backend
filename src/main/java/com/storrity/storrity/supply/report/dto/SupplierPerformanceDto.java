/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
public class SupplierPerformanceDto {

    private String supplierId;
    private String supplierName;

    private Long totalOrders;
    private Long onTimeOrders;
    private Long lateOrders;
    private Double onTimeDeliveryRate;

    private Double averageDelayDays;
    private Money totalSpend;
    private Money averageOrderValue;
    private Long avarageLeadTime; //Duration in hours

    public SupplierPerformanceDto(
            String supplierId,
            String supplierName,
            Long totalOrders,
            Long onTimeOrders,
            Long lateOrders,
            Double averageDelayDays,
            Long totalSpend) {

        this.supplierId = supplierId;
        this.supplierName = supplierName;

        this.totalOrders = totalOrders;
        this.onTimeOrders = onTimeOrders;
        this.lateOrders = lateOrders;
        this.onTimeDeliveryRate = (totalOrders == null || totalOrders == 0)
                ? null
                : (onTimeOrders == null ? 0D : onTimeOrders.doubleValue()) * 100.0 / totalOrders;

        this.averageDelayDays = averageDelayDays;
        this.totalSpend = Money.ofMicroNaira(totalSpend);
    }
}
