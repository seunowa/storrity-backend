/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.service;

import com.storrity.storrity.supply.entity.SupplyProcess;
import com.storrity.storrity.supply.entity.SupplyProcessSettings;
import com.storrity.storrity.supply.entity.SupplyProcessTemplate;
import com.storrity.storrity.supply.repository.SupplyProcessSettingsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
public class SupplyProcessSettingsServiceImpl implements SupplyProcessSettingsService{

    private final SupplyProcessSettingsRepository repository;

    public SupplyProcessSettingsServiceImpl(
            SupplyProcessSettingsRepository repository) {

        this.repository = repository;
    }

    @Override
    @Transactional
    public SupplyProcess updateSupplyProcessSettings(
            SupplyProcess supplyProcess) {

        if (supplyProcess == null) {
            throw new IllegalArgumentException(
                    "Supply process cannot be null"
            );
        }

        /*
         * Validate the new process before replacing
         * the existing configuration.
         */
        supplyProcess.validate();

        /*
         * Get the single system-wide settings record.
         *
         * If it does not exist, create it.
         */
        SupplyProcessSettings settings =
                repository.findTopByOrderByCreatedAtAsc()
                        .orElseGet(SupplyProcessSettings::new);

        /*
         * Completely override the existing process.
         */
        settings.setSupplyProcess(supplyProcess);

        /*
         * Existing entity -> UPDATE
         * New entity      -> INSERT
         */
        repository.save(settings);

        return settings.getSupplyProcess();
    }

    @Override
    @Transactional
    public SupplyProcess getSupplyProcessSettings() {

        return repository.findTopByOrderByCreatedAtAsc()
                .map(SupplyProcessSettings::getSupplyProcess)
                .orElse(null);
    }

    @Override
    public SupplyProcessTemplate getTemplates() {

        return new SupplyProcessTemplate();
    }
}