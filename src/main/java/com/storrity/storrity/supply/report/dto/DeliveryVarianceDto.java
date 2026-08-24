/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.report.dto;

import java.time.LocalDateTime;
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
public class DeliveryVarianceDto {
    private LocalDateTime receivedAt;

    private String supplierId;
    private String supplierName;

    private String productId;
    private String productName;
    private String productCode;

    private String storeId;
    private String storeName;

    private Double quantityOrdered;
    private Double quantityReceived;
    private Double quantityVariance;

    public DeliveryVarianceDto(
            LocalDateTime receivedAt,
            String supplierId,
            String supplierName,
            String productId,
            String productName,
            String productCode,
            String storeId,
            String storeName,
            Double quantityOrdered,
            Double quantityReceived,
            Double quantityVariance) {

        this.receivedAt = receivedAt;

        this.supplierId = supplierId;
        this.supplierName = supplierName;

        this.productId = productId;
        this.productName = productName;
        this.productCode = productCode;

        this.storeId = storeId;
        this.storeName = storeName;

        this.quantityOrdered = quantityOrdered;
        this.quantityReceived = quantityReceived;
        this.quantityVariance = quantityVariance;
    }
}
