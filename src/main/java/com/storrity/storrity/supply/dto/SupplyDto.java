/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.supply.entity.Supply;
import com.storrity.storrity.supply.entity.SupplyAction;
import com.storrity.storrity.supply.entity.SupplyProcess;
import com.storrity.storrity.supply.entity.SupplyStatus;
import com.storrity.storrity.supply.entity.SupplyTimeline;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
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
    
    private SupplyStatus supplyStatus;
    private String deliveryNoteNumber;
    private String invoiceNumber;
    
    private SupplyAction mostRecentSupplyAction;

    private String supplierId;
    private String supplierName;
    private String contactPerson;
    private String supplierPhone;
    private String supplierEmail;
    
    private Money grandTotal;    
    private String notes;
    
    private Collection<OrderItemDto> orderItems;
    private Collection<SupplyItemDto> supplyItems; 
    
    private Map<String, Object> metadata;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private SupplyProcess supplyProcess;
    private SupplyTimeline supplyTimeline;
    
    private LocalDate expectedSupplyDate;
    
    public static SupplyDto from(Supply s){
        return SupplyDto.builder()
                .id(s.getId())
                .transactionRef(s.getTransactionRef())
                .storeId(s.getStoreId())
                .storeName(s.getStoreName())
                .supplyStatus(s.getSupplyStatus())
                .deliveryNoteNumber(s.getDeliveryNoteNumber())
                .invoiceNumber(s.getInvoiceNumber())
                .mostRecentSupplyAction(s.getMostRecentSupplyAction())
                .supplierId(s.getSupplierId())
                .supplierName(s.getSupplierName())
                .contactPerson(s.getContactPerson())
                .supplierPhone(s.getSupplierPhone())
                .supplierEmail(s.getSupplierEmail())
                .grandTotal(s.getGrandTotal())
                .notes(s.getNotes())
                .orderItems(Optional.ofNullable(s.getOrderItems())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(OrderItemDto::from)
                        .collect(Collectors.toList()))
                .supplyItems(Optional.ofNullable(s.getSupplyItems())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(SupplyItemDto::from)
                        .collect(Collectors.toList()))
                .metadata(s.getMetadata())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .supplyProcess(s.getSupplyProcess())
                .supplyTimeline(s.getSupplyTimeline())
                .expectedSupplyDate(s.getExpectedSupplyDate())
                .build();
    }
}