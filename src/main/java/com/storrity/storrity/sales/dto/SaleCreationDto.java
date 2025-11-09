/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.sales.entity.PckQtyWithSellinPrice;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class SaleCreationDto {
    private UUID productId;
    private String performedBy;
    private Double quantity;
    private List<PckQtyWithSellinPrice> pckQty;
    private Money unitPrice;
    private Double discountRate;
    private Double taxRate;
    private String transactionRef;
}
