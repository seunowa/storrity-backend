/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.supply.entity.SupplyPaymentStatus;
import com.storrity.storrity.supply.entity.SupplyStatus;
import com.storrity.storrity.supply.entity.Supply;
import com.storrity.storrity.util.approval.ApprovalStatus;
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
public class SupplyDtoStale {
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
    private String notes;
    private String approvedBy;
    private Collection<SupplyItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ApprovalStatus approvalStatus;
    
    
    public static SupplyDtoStale from(Supply s){
        return SupplyDtoStale.builder()
                .id(s.getId())
                .transactionRef(s.getTransactionRef())
                .storeId(s.getStoreId())
                .storeName(s.getStoreName())
                .supplyDate(s.getExpectedSupplyDate())
//                .enteredByUserId(s.getDraftSubmittedByUserId())
//                .receivedByUserId(s.getReceivedByUserId())
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
                .notes(s.getNotes())
//                .approvedBy(s.getDeliveryApprovedByUserId())
                .items(s.getSupplyItems()
                        .stream()
                        .map(SupplyItemDto::from)
                        .collect(Collectors.toList()))
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
//                .approvalStatus(s.getApprovalStatus())
                .build();
    }
}
