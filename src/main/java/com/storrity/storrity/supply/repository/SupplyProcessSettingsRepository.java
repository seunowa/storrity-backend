/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.supply.repository;

import com.storrity.storrity.supply.entity.SupplyProcessSettings;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Seun Owa
 */
@Repository
public interface SupplyProcessSettingsRepository
        extends JpaRepository<SupplyProcessSettings, UUID> {

    Optional<SupplyProcessSettings> findTopByOrderByCreatedAtAsc();
}
