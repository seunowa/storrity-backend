/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cart.repository;

import com.storrity.storrity.cart.entity.CartItem;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Seun Owa
 */
public interface CartItemRepository extends JpaRepository<CartItem, UUID>{
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ci FROM CartItem ci WHERE ci.id = :id")
    Optional<CartItem> findByIdForUpdate(@Param("id") UUID id);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ci FROM CartItem ci WHERE ci.id IN :ids")
    List<CartItem> findAllByIdForUpdate(@Param("ids") Collection<UUID> ids);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ci FROM CartItem ci WHERE ci.cartId = :cartId AND ci.product.id = :productId")
    Optional<CartItem> findByCartIdAndProductIdForUpdate(@Param("cartId") UUID cartId, @Param("productId") UUID productId);
}
