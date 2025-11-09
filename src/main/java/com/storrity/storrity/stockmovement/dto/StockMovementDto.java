/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.stockmovement.dto;

import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stockmovement.entity.StockMovement;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
@Schema(description = "Stock movement response object")
public class StockMovementDto {
    private UUID id;
    private String description;
    private Double qtyIn; 
    private Double qtyOut;
    private Double balance;
    private UUID productId;
    private String productName;
    private String productCode;
    private String performedBy;
    private String transactionRef;
    private String sku;
    private LocalDateTime createdAt;
    private List<PckQty> pckQty;
    private Map<String, Object> metadata;
    
    public static StockMovementDto from(StockMovement sm) {        
        return StockMovementDto.builder()
                .id(sm.getId())
                .description(sm.getDescription())
                .qtyIn(sm.getQtyIn())
                .qtyOut(sm.getQtyOut())
                .balance(sm.getBalance())
                .productId(sm.getProduct().getId())
                .productName(sm.getProduct().getName())
                .productCode(sm.getProduct().getCode())
                .performedBy(sm.getPerformedBy())
                .transactionRef(sm.getTransactionRef())
                .sku(sm.getSku())
                .createdAt(sm.getCreatedAt())
                .pckQty(sm.getPckQty())
                .metadata(sm.getMetadata())
                .build();
    }
}
