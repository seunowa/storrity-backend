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

import java.util.List;

/**
 * Application-facing service for cross-cutting inventory reports.
 * 
 * Implementation is intentionally deferred. The service should remain thin: validate/normalize the report-specific parameters, apply application-level rules where necessary, and delegate query execution to InventoryReportRepository.
 */
public interface InventoryReportService {

    /** Intended implementation: delegate to the corresponding repository method. */
    InventorySummaryDto inventorySummary(InventorySummaryQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<InventoryByProductDto> inventoryByProduct(InventoryByProductQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<InventoryByStoreDto> inventoryByStore(InventoryByStoreQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<InventoryByCategoryDto> inventoryByCategory(InventoryByCategoryQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<InventoryByBrandDto> inventoryByBrand(InventoryByBrandQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<InventoryByProductTypeDto> inventoryByProductType(InventoryByProductTypeQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<LowStockInventoryDto> lowStockInventory(LowStockInventoryQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<OutOfStockInventoryDto> outOfStockInventory(OutOfStockInventoryQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<OverstockInventoryDto> overstockInventory(OverstockInventoryQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<InventoryReorderRecommendationDto> inventoryReorderRecommendations(InvReorderRecQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<ExpiringInventoryDto> expiringInventory(ExpiringInventoryQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<ExpiredInventoryDto> expiredInventory(ExpiredInventoryQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<InventoryByBatchDto> inventoryByBatch(InventoryByBatchQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<SlowMovingInventoryDto> slowMovingInventory(SlowMovingInventoryQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<DeadStockInventoryDto> deadStockInventory(DeadStockInventoryQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<InventoryDaysOfSupplyDto> inventoryDaysOfSupply(InventoryDaysOfSupplyQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<InventoryTurnoverDto> inventoryTurnover(InventoryTurnoverQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<InventoryBalanceReconciliationDto> inventoryBalanceReconciliation(InvBalanceRecQueryParams params);

    /** Intended implementation: delegate to the corresponding repository method. */
    List<InventoryInTransitDto> inventoryInTransit(InventoryInTransitQueryParams params);

}
