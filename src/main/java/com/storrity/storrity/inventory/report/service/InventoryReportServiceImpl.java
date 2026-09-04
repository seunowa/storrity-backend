/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.inventory.report.service;

import com.storrity.storrity.inventory.report.dto.DeadStockInventoryDto;
import com.storrity.storrity.inventory.report.dto.DeadStockInventoryQueryParams;
import com.storrity.storrity.inventory.report.dto.ExpiredInventoryDto;
import com.storrity.storrity.inventory.report.dto.ExpiredInventoryQueryParams;
import com.storrity.storrity.inventory.report.dto.ExpiringInventoryDto;
import com.storrity.storrity.inventory.report.dto.ExpiringInventoryQueryParams;
import com.storrity.storrity.inventory.report.dto.InventoryBalanceReconciliationDto;
import com.storrity.storrity.inventory.report.dto.InvBalanceRecQueryParams;
import com.storrity.storrity.inventory.report.dto.InventoryByBatchDto;
import com.storrity.storrity.inventory.report.dto.InventoryByBatchQueryParams;
import com.storrity.storrity.inventory.report.dto.InventoryByBrandDto;
import com.storrity.storrity.inventory.report.dto.InventoryByBrandQueryParams;
import com.storrity.storrity.inventory.report.dto.InventoryByCategoryDto;
import com.storrity.storrity.inventory.report.dto.InventoryByCategoryQueryParams;
import com.storrity.storrity.inventory.report.dto.InventoryByProductDto;
import com.storrity.storrity.inventory.report.dto.InventoryByProductQueryParams;
import com.storrity.storrity.inventory.report.dto.InventoryByProductTypeDto;
import com.storrity.storrity.inventory.report.dto.InventoryByProductTypeQueryParams;
import com.storrity.storrity.inventory.report.dto.InventoryByStoreDto;
import com.storrity.storrity.inventory.report.dto.InventoryByStoreQueryParams;
import com.storrity.storrity.inventory.report.dto.InventoryDaysOfSupplyDto;
import com.storrity.storrity.inventory.report.dto.InventoryDaysOfSupplyQueryParams;
import com.storrity.storrity.inventory.report.dto.InventoryInTransitDto;
import com.storrity.storrity.inventory.report.dto.InventoryInTransitQueryParams;
import com.storrity.storrity.inventory.report.dto.InventoryReorderRecommendationDto;
import com.storrity.storrity.inventory.report.dto.InvReorderRecQueryParams;
import com.storrity.storrity.inventory.report.dto.InventorySummaryDto;
import com.storrity.storrity.inventory.report.dto.InventorySummaryQueryParams;
import com.storrity.storrity.inventory.report.dto.InventoryTurnoverDto;
import com.storrity.storrity.inventory.report.dto.InventoryTurnoverQueryParams;
import com.storrity.storrity.inventory.report.dto.LowStockInventoryDto;
import com.storrity.storrity.inventory.report.dto.LowStockInventoryQueryParams;
import com.storrity.storrity.inventory.report.dto.OutOfStockInventoryDto;
import com.storrity.storrity.inventory.report.dto.OutOfStockInventoryQueryParams;
import com.storrity.storrity.inventory.report.dto.OverstockInventoryDto;
import com.storrity.storrity.inventory.report.dto.OverstockInventoryQueryParams;
import com.storrity.storrity.inventory.report.dto.SlowMovingInventoryDto;
import com.storrity.storrity.inventory.report.dto.SlowMovingInventoryQueryParams;
import com.storrity.storrity.inventory.report.repository.InventoryReportRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 * @author Seun Owa
 */
@Service
public class InventoryReportServiceImpl implements InventoryReportService{
    
    private InventoryReportRepository inventoryReportRepository;

    @Autowired
    public InventoryReportServiceImpl(InventoryReportRepository inventoryReportRepository) {
        this.inventoryReportRepository = inventoryReportRepository;
    }

    @Override
    public InventorySummaryDto inventorySummary(InventorySummaryQueryParams params) {
        return inventoryReportRepository.inventorySummary(params);
    }

    @Override
    public List<InventoryByProductDto> inventoryByProduct(InventoryByProductQueryParams params) {
        return inventoryReportRepository.inventoryByProduct(params);
    }

    @Override
    public List<InventoryByStoreDto> inventoryByStore(InventoryByStoreQueryParams params) {
        return inventoryReportRepository.inventoryByStore(params);
    }

    @Override
    public List<InventoryByCategoryDto> inventoryByCategory(InventoryByCategoryQueryParams params) {
        return inventoryReportRepository.inventoryByCategory(params);
    }

    @Override
    public List<InventoryByBrandDto> inventoryByBrand(InventoryByBrandQueryParams params) {
        return inventoryReportRepository.inventoryByBrand(params);
    }

    @Override
    public List<InventoryByProductTypeDto> inventoryByProductType(InventoryByProductTypeQueryParams params) {
        return inventoryReportRepository.inventoryByProductType(params);
    }

    @Override
    public List<LowStockInventoryDto> lowStockInventory(LowStockInventoryQueryParams params) {
        return inventoryReportRepository.lowStockInventory(params);
    }

    @Override
    public List<OutOfStockInventoryDto> outOfStockInventory(OutOfStockInventoryQueryParams params) {
        return inventoryReportRepository.outOfStockInventory(params);
    }

    @Override
    public List<OverstockInventoryDto> overstockInventory(OverstockInventoryQueryParams params) {
        return inventoryReportRepository.overstockInventory(params);
    }

    @Override
    public List<InventoryReorderRecommendationDto> inventoryReorderRecommendations(InvReorderRecQueryParams params) {
        return inventoryReportRepository.inventoryReorderRecommendations(params);
    }

    @Override
    public List<ExpiringInventoryDto> expiringInventory(ExpiringInventoryQueryParams params) {
        return inventoryReportRepository.expiringInventory(params);
    }

    @Override
    public List<ExpiredInventoryDto> expiredInventory(ExpiredInventoryQueryParams params) {
        return inventoryReportRepository.expiredInventory(params);
    }

    @Override
    public List<InventoryByBatchDto> inventoryByBatch(InventoryByBatchQueryParams params) {
        return inventoryReportRepository.inventoryByBatch(params);
    }

    @Override
    public List<SlowMovingInventoryDto> slowMovingInventory(SlowMovingInventoryQueryParams params) {
        return inventoryReportRepository.slowMovingInventory(params);
    }

    @Override
    public List<DeadStockInventoryDto> deadStockInventory(DeadStockInventoryQueryParams params) {
        return inventoryReportRepository.deadStockInventory(params);
    }

    @Override
    public List<InventoryDaysOfSupplyDto> inventoryDaysOfSupply(InventoryDaysOfSupplyQueryParams params) {
        return inventoryReportRepository.inventoryDaysOfSupply(params);
    }

    @Override
    public List<InventoryTurnoverDto> inventoryTurnover(InventoryTurnoverQueryParams params) {
        return inventoryReportRepository.inventoryTurnover(params);
    }

    @Override
    public List<InventoryBalanceReconciliationDto> inventoryBalanceReconciliation(InvBalanceRecQueryParams params) {
        return inventoryReportRepository.inventoryBalanceReconciliation(params);
    }

    @Override
    public List<InventoryInTransitDto> inventoryInTransit(InventoryInTransitQueryParams params) {
        return inventoryReportRepository.inventoryInTransit(params);
    }
}
