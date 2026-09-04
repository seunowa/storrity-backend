package com.storrity.storrity.inventory.report.repository;

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
 * Cross-cutting inventory reporting repository.
 * 
 * Implementation is intentionally deferred. Each method should build its query from the tailored parameter object for that report.
 * Current-state reports should primarily use the Product snapshot; historical/derived reports should use StockMovement
 * and, where required, StockTransfer/Supply data.
 */
public interface InventoryReportRepository {

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    InventorySummaryDto inventorySummary(InventorySummaryQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<InventoryByProductDto> inventoryByProduct(InventoryByProductQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<InventoryByStoreDto> inventoryByStore(InventoryByStoreQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<InventoryByCategoryDto> inventoryByCategory(InventoryByCategoryQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<InventoryByBrandDto> inventoryByBrand(InventoryByBrandQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<InventoryByProductTypeDto> inventoryByProductType(InventoryByProductTypeQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<LowStockInventoryDto> lowStockInventory(LowStockInventoryQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<OutOfStockInventoryDto> outOfStockInventory(OutOfStockInventoryQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<OverstockInventoryDto> overstockInventory(OverstockInventoryQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<InventoryReorderRecommendationDto> inventoryReorderRecommendations(InvReorderRecQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<ExpiringInventoryDto> expiringInventory(ExpiringInventoryQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<ExpiredInventoryDto> expiredInventory(ExpiredInventoryQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<InventoryByBatchDto> inventoryByBatch(InventoryByBatchQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<SlowMovingInventoryDto> slowMovingInventory(SlowMovingInventoryQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<DeadStockInventoryDto> deadStockInventory(DeadStockInventoryQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<InventoryDaysOfSupplyDto> inventoryDaysOfSupply(InventoryDaysOfSupplyQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<InventoryTurnoverDto> inventoryTurnover(InventoryTurnoverQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<InventoryBalanceReconciliationDto> inventoryBalanceReconciliation(InvBalanceRecQueryParams params);

    /** Intended implementation: execute the report-specific inventory query and map rows to the DTO. */
    List<InventoryInTransitDto> inventoryInTransit(InventoryInTransitQueryParams params);

}
