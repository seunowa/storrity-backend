/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.supply.service;

import com.storrity.storrity.supply.entity.SupplyProcess;
import com.storrity.storrity.supply.entity.SupplyProcessTemplate;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface SupplyProcessSettingsService {
    public SupplyProcess updateSupplyProcessSettings(SupplyProcess supplyProcesses);
    public SupplyProcess getSupplyProcessSettings();
    public SupplyProcessTemplate getTemplates();
}
