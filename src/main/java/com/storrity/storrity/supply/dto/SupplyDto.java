/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.entity.SupplyPaymentStatus;
import com.storrity.storrity.product.entity.SupplyStatus;
import com.storrity.storrity.supply.entity.Supply;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Data
@SuperBuilder
@Schema(description = "Supply response object")
public class SupplyDto {
    private UUID id;  
    private String transactionRef;
    private UUID storeId;
    private String storeName;
    private LocalDate supplyDate;
    private String enteredByUserId;
    private String receivedByUserId;
    private SupplyStatus supplyStatus;
    private String deliveryNoteNumber;
    private String invoiceNumber;
    private String supplierId;
    private String supplierName;
    private String contactPerson;
    private String supplierPhone;
    private String supplierEmail;
    private Map<String, Object> metadata;
    private Money grandTotal;
    private Money deliveryFee;
    private String paymentMethod; 
    private Money amountPaid;
    private String notes;
    private String approvedByUserId;
    private Collection<SupplyItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    
    public static SupplyDto from(Supply s){
        return SupplyDto.builder()
                .id(s.getId())
                .transactionRef(s.getTransactionRef())
                .storeId(s.getStore().getId())
                .storeName(s.getStore().getName())
                .supplyDate(s.getSupplyDate())
                .enteredByUserId(s.getEnteredByUserId())
                .receivedByUserId(s.getReceivedByUserId())
                .supplyStatus(s.getSupplyStatus())
                .deliveryNoteNumber(s.getDeliveryNoteNumber())
                .invoiceNumber(s.getInvoiceNumber())
                .supplierId(s.getSupplierId())
                .supplierName(s.getSupplierName())
                .contactPerson(s.getContactPerson())
                .supplierPhone(s.getSupplierPhone())
                .supplierEmail(s.getSupplierEmail())
                .metadata(s.getMetadata())
                .grandTotal(s.getGrandTotal())
                .deliveryFee(s.getDeliveryFee())
                .paymentMethod(s.getPaymentMethod()) 
                .amountPaid(s.getAmountPaid())
                .notes(s.getNotes())
                .approvedByUserId(s.getApprovedByUserId())
                .items(s.getItems()
                        .stream()
                        .map(SupplyItemDto::from)
                        .collect(Collectors.toList()))
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}
