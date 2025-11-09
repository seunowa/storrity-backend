/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.dto;

import com.storrity.storrity.stockmovement.entity.PckQty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Sales return response object")
public class SalesReturnDto {
    private UUID id;
    private String transactionRef;
    private UUID saleId;
    private Double quantity;
    private String sku;
    private List<PckQty> pckQty;
    private String reason;
    private String performedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; 
}
