/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.salesattendant.repository;

import com.storrity.storrity.salesattendant.entity.SalesAttendant;
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
public interface SalesAttendantRepository extends JpaRepository<SalesAttendant, UUID>, SalesAttendantRepositoryCustom{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SalesAttendant s WHERE s.id = :id")
    Optional<SalesAttendant> findByIdForUpdate(@Param("id") UUID id);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SalesAttendant s WHERE s.id IN :ids")
        List<SalesAttendant> findAllByIdForUpdate(@Param("ids") Collection<UUID> ids);
        
    @Query("SELECT s FROM SalesAttendant s WHERE s.id IN :ids")
        List<SalesAttendant> findAllById(@Param("ids") Collection<UUID> ids);
        
    Optional<SalesAttendant> findByUsername(String username);
    
}
