/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.supply.repository;

import com.storrity.storrity.supply.dto.SupplyQueryParams;
import com.storrity.storrity.supply.entity.Supply;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface SupplyRepositoryCustom {    
    public List<Supply> list(SupplyQueryParams params);
    public Long countRecords (SupplyQueryParams params);
}
