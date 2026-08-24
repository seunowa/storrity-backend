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
@AllArgsConstructor
public class SupplierLeadTimeDto {
    private String supplierId;
    private String supplierName;

    private Long ordersFulfilled;
    private Double minLeadTimeDays;
    private Double averageLeadTimeDays;
    private Double medianLeadTimeDays;
    private Double maxLeadTimeDays;
}
