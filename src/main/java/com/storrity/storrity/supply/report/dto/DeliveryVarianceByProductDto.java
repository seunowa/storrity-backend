/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.report.dto;

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
public class DeliveryVarianceByProductDto {
    private String productId;
    private String productName;
    private String productCode;

    private Double quantityOrdered;
    private Double quantityReceived;
    private Double quantityVariance;

    public DeliveryVarianceByProductDto(
            String productId,
            String productName,
            String productCode,
            Double quantityOrdered,
            Double quantityReceived,
            Double quantityVariance) {

        this.productId = productId;
        this.productName = productName;
        this.productCode = productCode;

        this.quantityOrdered = quantityOrdered;
        this.quantityReceived = quantityReceived;
        this.quantityVariance = quantityVariance;
    }
}
