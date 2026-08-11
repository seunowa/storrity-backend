/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.repository;

import com.storrity.storrity.ai.settings.entity.BusinessContext;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Seun Owa
 */
public interface BusinessContextRepository extends JpaRepository<BusinessContext, UUID> {

    Optional<BusinessContext> findByStoreId(UUID storeId);

    Optional<BusinessContext> findByStoreIdIsNull(); // Global
}
