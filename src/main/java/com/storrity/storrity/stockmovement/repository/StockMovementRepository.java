/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.stockmovement.repository;

import com.storrity.storrity.stockmovement.entity.StockMovement;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Seun Owa
 */
public interface StockMovementRepository extends JpaRepository<StockMovement, UUID>, StockMovementRepositoryCustom{
    
}
