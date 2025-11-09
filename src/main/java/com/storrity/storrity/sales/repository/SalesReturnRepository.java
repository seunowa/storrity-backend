/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.sales.repository;

import com.storrity.storrity.sales.entity.SalesReturn;
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
public interface SalesReturnRepository extends JpaRepository<SalesReturn, UUID>, SalesReturnRepositoryCustom{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SalesReturn s WHERE s.id = :id")
    Optional<SalesReturn> findByIdForUpdate(@Param("id") UUID id);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SalesReturn s WHERE s.id IN :ids")
        List<SalesReturn> findAllByIdForUpdate(@Param("ids") Collection<UUID> ids);
}
