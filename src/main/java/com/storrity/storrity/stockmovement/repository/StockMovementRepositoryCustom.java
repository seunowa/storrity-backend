/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.stockmovement.repository;

import com.storrity.storrity.stockmovement.entity.StockMovement;
import com.storrity.storrity.stockmovement.entity.StockMovementQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface StockMovementRepositoryCustom {
    public List<StockMovement> list(StockMovementQueryParams params);
    public Long countRecords (StockMovementQueryParams params);
    
}
