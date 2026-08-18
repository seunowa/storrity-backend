/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.stockmovement.dto;

import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stockmovement.entity.StockMoevmentDirection;
import java.time.LocalDate;
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
public class StockMovementInstructionItem {
    private Double quantity;
    private StockMoevmentDirection flow;
    private UUID productId;
    @Deprecated
    private String productName;
    @Deprecated
    private String productCode;
    @Deprecated
    private String productCategory;
    @Deprecated
    private String productSubCategory;
    @Deprecated
    private UUID storeId;
    @Deprecated
    private String storeName;
    private String performedBy;
    private List<PckQty> pckQty;
    private String batchNumber;
    private LocalDate expiryDate;
}
