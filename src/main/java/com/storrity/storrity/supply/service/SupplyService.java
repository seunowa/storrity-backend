/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.supply.service;

import com.storrity.storrity.supply.dto.PurchaseOrderCreationDto;
import com.storrity.storrity.supply.dto.DeliveryDto;
import com.storrity.storrity.supply.dto.SupplyDto;
import com.storrity.storrity.supply.dto.SupplyQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface SupplyService {
    public SupplyDto createDraft(PurchaseOrderCreationDto dto);

    SupplyDto fetch(UUID id);

    List<SupplyDto> list(SupplyQueryParams params);

    CountDto count(SupplyQueryParams params);

    SupplyDto updateDraft(UUID id, PurchaseOrderCreationDto dto);

    SupplyDto submitDraft(UUID id);

    SupplyDto approveDraft(UUID id);

    SupplyDto order(UUID id);

    SupplyDto deliver(UUID id, DeliveryDto dto);

    SupplyDto submitDelivery(UUID id);

    SupplyDto approveDelivery(UUID id);

    SupplyDto receive(UUID id);

    SupplyDto cancel(UUID id);

    SupplyDto delete(UUID id);
}
