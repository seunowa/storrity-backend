/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.supply.repository;

import com.storrity.storrity.supply.entity.SupplyItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Seun Owa
 */
public interface SupplyItemRepository extends JpaRepository<SupplyItem, UUID>, SupplyItemRepositoryCustom{
    
}
