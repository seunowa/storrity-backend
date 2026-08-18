/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.supply.repository;

import com.storrity.storrity.util.entity.OrderItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Seun Owa
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID>, OrderItemRepositoryCustom{
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from OrderItem s where s.supplyId = :supplyId")
    void deleteBySupplyId(UUID supplyId);
}
