/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.stockmovement.service;

import com.storrity.storrity.stockmovement.dto.StockMovementDto;
import com.storrity.storrity.stockmovement.dto.StockMovementInstruction;
import com.storrity.storrity.stockmovement.dto.StockMovementResult;
import com.storrity.storrity.stockmovement.entity.StockMovementQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface StockMovementService {
    public StockMovementResult create(StockMovementInstruction dto);
    public StockMovementDto fetch(UUID id);
    public List<StockMovementDto> list(StockMovementQueryParams params);
    public CountDto count(StockMovementQueryParams params);
}
