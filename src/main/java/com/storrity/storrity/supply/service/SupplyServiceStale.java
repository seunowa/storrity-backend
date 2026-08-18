/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.supply.service;

import com.storrity.storrity.supply.dto.SupplyCreationDtoStale;
import com.storrity.storrity.supply.dto.SupplyDtoStale;
import com.storrity.storrity.supply.dto.SupplyQueryParams;
import com.storrity.storrity.supply.dto.SupplyStatusUpdateDtoStale;
import com.storrity.storrity.supply.dto.SupplyUpdateDtoStale;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface SupplyServiceStale {
    public SupplyDtoStale create(SupplyCreationDtoStale dto);
    public SupplyDtoStale fetch(UUID id);
    public List<SupplyDtoStale> list(SupplyQueryParams params);
    public CountDto count(SupplyQueryParams params);
    public SupplyDtoStale update(UUID id, SupplyUpdateDtoStale dto);
    public SupplyDtoStale delete(UUID id);
    public SupplyDtoStale updateStatus(UUID id, SupplyStatusUpdateDtoStale dto);
}
