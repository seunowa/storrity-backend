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
public class ProductProcurementSummaryDto {    
    private Double quantityOrdered;
    private Double quantityReceived;
    private Double qantityVariance;
    private Money totalSpend;
    private Integer numberOfSuppliers;
    private Integer numberOfPurchases;
    private Money averagePurchasePrice;
    private Money lowestPurchasePrice;
    private String lowestPurchasePriceSupplierName;
    private Money highestPurchasePrice;
    private String highestPurchasePriceSupplierName;

    public ProductProcurementSummaryDto(Double quantityOrdered,
            Double quantityReceived,
            Double qantityVariance,
            Long totalSpend,
            Integer numberOfSuppliers, 
            Integer numberOfPurchases,
            Long averagePurchasePrice,
            Long lowestPurchasePrice,
            String lowestPurchasePriceSupplierName,
            Long highestPurchasePrice,
            String highestPurchasePriceSupplierName) {
        this.quantityOrdered = quantityOrdered;
        this.quantityReceived = quantityReceived;
        this.qantityVariance = qantityVariance;
        this.totalSpend = Money.ofMicroNaira(totalSpend);
        this.numberOfSuppliers = numberOfSuppliers;
        this.numberOfPurchases = numberOfPurchases;
        this.averagePurchasePrice = Money.ofMicroNaira(averagePurchasePrice);
        this.lowestPurchasePrice = Money.ofMicroNaira(lowestPurchasePrice);
        this.lowestPurchasePriceSupplierName = lowestPurchasePriceSupplierName;
        this.highestPurchasePrice = Money.ofMicroNaira(highestPurchasePrice);
        this.highestPurchasePriceSupplierName = highestPurchasePriceSupplierName;
    }
    
    
}
