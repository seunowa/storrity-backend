/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.stockmovement.entity.PckQty;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;

/**
 *
 * @author Seun Owa
 */
@Data
public class DeliveryItemDto {

    private UUID orderItemId; // nullable

    private UUID productId;

    private Double quantityReceived;

    private String batchNumber;

    private LocalDate expiryDate;

    private List<PckQty> pckQty;

    private Money costPrice;
}
