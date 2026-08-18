/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.stockmovement.dto;

import com.storrity.storrity.stockmovement.entity.StockMovementType;
import java.util.List;
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
public class StockMovementInstruction {
    private String description;
    private String transactionRef;
    private String performedBy;
    private StockMovementType movementType;
    private List<StockMovementInstructionItem> instructionItems;
}
