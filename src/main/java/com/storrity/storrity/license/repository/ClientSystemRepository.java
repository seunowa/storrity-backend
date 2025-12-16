/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.license.repository;

import com.storrity.storrity.license.entity.ClientSystem;
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
public interface ClientSystemRepository extends JpaRepository<ClientSystem, UUID>, ClientSystemRepositoryCustom{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM ClientSystem c WHERE c.id = :id")
    Optional<ClientSystem> findByIdForUpdate(@Param("id") UUID id);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM ClientSystem c WHERE c.id IN :ids")
        List<ClientSystem> findAllByIdForUpdate(@Param("ids") Collection<UUID> ids);
        
    @Query("SELECT c FROM ClientSystem c WHERE c.id IN :ids")
        List<ClientSystem> findAllById(@Param("ids") Collection<UUID> ids);
        
    @Query("SELECT c FROM ClientSystem c WHERE c.clientId = :clientId")
    Optional<ClientSystem> findByClientId(@Param("clientId") String clientId);
        
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM ClientSystem c WHERE c.clientId = :clientId")
    Optional<ClientSystem> findByClientIdForUpdate(@Param("clientId") String clientId);
}
