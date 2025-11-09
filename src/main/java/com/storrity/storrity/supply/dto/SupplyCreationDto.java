/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.entity.SupplyStatus;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SupplyCreationDto {
    private String transactionRef;
    private UUID storeId;
    private LocalDate supplyDate;
    private SupplyStatus supplyStatus;
    private String deliveryNoteNumber;
    private String invoiceNumber;
    private String supplierId;
    private String supplierName;
    private String contactPerson;
    private String supplierPhone;
    private String supplierEmail;
    private Money deliveryFee;
    private String paymentMethod; 
    private Money amountPaid;
//    private Money costPrice;
    private String notes;
    private String enteredByUserId;
    private String receivedByUserId;
    private String approvedByUserId;
    private Collection<SupplyItemCreationDto> items;    
}
