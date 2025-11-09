/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.supply.service;

import com.storrity.storrity.supply.dto.SupplyCreationDto;
import com.storrity.storrity.supply.dto.SupplyDto;
import com.storrity.storrity.supply.dto.SupplyQueryParams;
import com.storrity.storrity.supply.dto.SupplyUpdateDto;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface SupplyService {
    public SupplyDto create(SupplyCreationDto dto);
    public SupplyDto fetch(UUID id);
    public List<SupplyDto> list(SupplyQueryParams params);
    public CountDto count(SupplyQueryParams params);
    public SupplyDto update(UUID id, SupplyUpdateDto dto);
    public SupplyDto delete(UUID id);
}
