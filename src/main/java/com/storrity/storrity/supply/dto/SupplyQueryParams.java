/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.dto;

import com.storrity.storrity.product.entity.SupplyPaymentStatus;
import com.storrity.storrity.product.entity.SupplyStatus;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class SupplyQueryParams {
    private List<UUID> ids; 
    private List<String> supplyReferences;
    private List<UUID> storeIds;
    @Size(min = 2, max = 2, message = "supplyDateRange must contain exactly two dates")
    private List<LocalDate> supplyDateRange;
    private List<String> enteredByUserIds;
    private List<String> receivedByUserIds;
    private List<SupplyStatus> supplyStatus;
    private List<String> deliveryNoteNumbers;
    private List<String> invoiceNumbers;
    private List<String> supplierIds;
    private List<String> supplierNames;
    private List<String> contactPersons;
    private List<String> supplierPhones;
    private List<String> supplierEmails;
    private SupplyPaymentStatus paymentStatus;
    private List<String> paymentMethods;
    private List<String> approvedByUserIds;
    @Size(min = 2, max = 2, message = "createdAtRange must contain exactly two dates")
    private List<LocalDateTime> createdAtRange;
    @Size(min = 2, max = 2, message = "updatedAtRange must contain exactly two dates")
    private List<LocalDateTime> updatedAtRange;
    private Integer offset;
    private Integer limit;
    private String searchPhrase;
    private String sort;
}
