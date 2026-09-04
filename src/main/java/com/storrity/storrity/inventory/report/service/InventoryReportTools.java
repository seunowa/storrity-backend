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
import com.storrity.storrity.product.entity.ProductType;
import com.storrity.storrity.product.entity.StockStatus;
import com.storrity.storrity.stocktransfer.entity.StockTransferStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Spring AI tools for inventory reporting.
 *
 * <p>The AI-facing tool methods expose individual report parameters through
 * {@link ToolParam}. The corresponding query parameter DTOs are constructed
 * internally before invoking the inventory report service.</p>
 *
 * @author Seun Owa
 */
@Component
public class InventoryReportTools {

    private final InventoryReportService inventoryReportService;

    @Autowired
    public InventoryReportTools(InventoryReportService inventoryReportService) {
        this.inventoryReportService = inventoryReportService;
    }

    @Tool(
        name = "get_inventory_summary",
        description = """
            Get a summary of current inventory.

            Use this to answer questions about total inventory quantity,
            inventory value, number of products, or a high-level inventory
            position. Filters can be applied by store, product, category,
            sub-category, brand, product type, or stock status.
            """
    )
    public InventorySummaryDto inventorySummary(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product sub-categories to include. Optional.")
            List<String> productSubCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Product types to include. Optional.")
            List<ProductType> productTypes,

            @ToolParam(description = "Stock statuses to include. Optional.")
            List<StockStatus> stockStatuses,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventorySummary(
                InventorySummaryQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productSubCategories(productSubCategories)
                        .productBrands(productBrands)
                        .productTypes(productTypes)
                        .stockStatuses(stockStatuses)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_inventory_by_product",
        description = """
            Get current inventory grouped by product.

            Use this when the user asks how much inventory is available for
            products, inventory quantities or values by product, or the
            current stock position of specific products.
            """
    )
    public List<InventoryByProductDto> inventoryByProduct(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product sub-categories to include. Optional.")
            List<String> productSubCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Product types to include. Optional.")
            List<ProductType> productTypes,

            @ToolParam(description = "Stock statuses to include. Optional.")
            List<StockStatus> stockStatuses,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventoryByProduct(
                InventoryByProductQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productSubCategories(productSubCategories)
                        .productBrands(productBrands)
                        .productTypes(productTypes)
                        .stockStatuses(stockStatuses)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_inventory_by_store",
        description = """
            Get current inventory summarized by store.

            Use this when the user asks about inventory at a particular store,
            inventory comparisons between stores, or inventory quantities and
            values by store.
            """
    )
    public List<InventoryByStoreDto> inventoryByStore(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Product types to include. Optional.")
            List<ProductType> productTypes,

            @ToolParam(description = "Stock statuses to include. Optional.")
            List<StockStatus> stockStatuses,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventoryByStore(
                InventoryByStoreQueryParams.builder()
                        .storeIds(storeIds)
                        .productCategories(productCategories)
                        .productBrands(productBrands)
                        .productTypes(productTypes)
                        .stockStatuses(stockStatuses)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_inventory_by_category",
        description = """
            Get current inventory summarized by product category.

            Use this when the user asks for inventory by category or wants to
            compare inventory quantity or value between categories.
            """
    )
    public List<InventoryByCategoryDto> inventoryByCategory(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Product types to include. Optional.")
            List<ProductType> productTypes,

            @ToolParam(description = "Stock statuses to include. Optional.")
            List<StockStatus> stockStatuses,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventoryByCategory(
                InventoryByCategoryQueryParams.builder()
                        .storeIds(storeIds)
                        .productCategories(productCategories)
                        .productBrands(productBrands)
                        .productTypes(productTypes)
                        .stockStatuses(stockStatuses)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_inventory_by_brand",
        description = """
            Get current inventory summarized by brand.

            Use this when the user asks about inventory for a brand or wants
            inventory quantities or values compared across brands.
            """
    )
    public List<InventoryByBrandDto> inventoryByBrand(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product types to include. Optional.")
            List<ProductType> productTypes,

            @ToolParam(description = "Stock statuses to include. Optional.")
            List<StockStatus> stockStatuses,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventoryByBrand(
                InventoryByBrandQueryParams.builder()
                        .storeIds(storeIds)
                        .productBrands(productBrands)
                        .productCategories(productCategories)
                        .productTypes(productTypes)
                        .stockStatuses(stockStatuses)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_inventory_by_product_type",
        description = """
            Get current inventory summarized by product type.

            Use this when the user asks for inventory by product type or wants
            to compare stock between product types.
            """
    )
    public List<InventoryByProductTypeDto> inventoryByProductType(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product types to include. Optional.")
            List<ProductType> productTypes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Stock statuses to include. Optional.")
            List<StockStatus> stockStatuses,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventoryByProductType(
                InventoryByProductTypeQueryParams.builder()
                        .storeIds(storeIds)
                        .productTypes(productTypes)
                        .productCategories(productCategories)
                        .productBrands(productBrands)
                        .stockStatuses(stockStatuses)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_low_stock_inventory",
        description = """
            Get products whose current inventory is at or below their
            configured low-stock threshold.

            Use this when the user asks which products are running low or
            which inventory needs replenishment.
            """
    )
    public List<LowStockInventoryDto> lowStockInventory(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product sub-categories to include. Optional.")
            List<String> productSubCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Product types to include. Optional.")
            List<ProductType> productTypes,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.lowStockInventory(
                LowStockInventoryQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productSubCategories(productSubCategories)
                        .productBrands(productBrands)
                        .productTypes(productTypes)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_out_of_stock_inventory",
        description = """
            Get products that currently have no inventory.

            Use this when the user asks which products are out of stock,
            unavailable, or have zero inventory.
            """
    )
    public List<OutOfStockInventoryDto> outOfStockInventory(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product sub-categories to include. Optional.")
            List<String> productSubCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Product types to include. Optional.")
            List<ProductType> productTypes,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.outOfStockInventory(
                OutOfStockInventoryQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productSubCategories(productSubCategories)
                        .productBrands(productBrands)
                        .productTypes(productTypes)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_overstock_inventory",
        description = """
            Get products whose inventory exceeds their configured maximum
            stock level.

            Use this when the user asks which products are overstocked or
            carrying excessive inventory.
            """
    )
    public List<OverstockInventoryDto> overstockInventory(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product sub-categories to include. Optional.")
            List<String> productSubCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Product types to include. Optional.")
            List<ProductType> productTypes,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.overstockInventory(
                OverstockInventoryQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productSubCategories(productSubCategories)
                        .productBrands(productBrands)
                        .productTypes(productTypes)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_inventory_reorder_recommendations",
        description = """
            Get products recommended for replenishment or reorder.

            Use this when the user asks what products should be reordered,
            what inventory needs replenishment, or which products require
            purchasing.
            """
    )
    public List<InventoryReorderRecommendationDto> inventoryReorderRecommendations(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product sub-categories to include. Optional.")
            List<String> productSubCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Product types to include. Optional.")
            List<ProductType> productTypes,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventoryReorderRecommendations(InvReorderRecQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productSubCategories(productSubCategories)
                        .productBrands(productBrands)
                        .productTypes(productTypes)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_expiring_inventory",
        description = """
            Get inventory that is approaching its expiration date.

            Use this when the user asks what inventory is about to expire or
            which batches require attention before expiry.
            """
    )
    public List<ExpiringInventoryDto> expiringInventory(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Batch numbers to include. Optional.")
            List<String> batchNumbers,

            @ToolParam(description = "Expiration date range containing exactly two dates. Optional.")
            List<LocalDate> expiryDateRange,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.expiringInventory(
                ExpiringInventoryQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productBrands(productBrands)
                        .batchNumbers(batchNumbers)
                        .expiryDateRange(expiryDateRange)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_expired_inventory",
        description = """
            Get inventory that has already expired.

            Use this when the user asks which products or batches have passed
            their expiration date.
            """
    )
    public List<ExpiredInventoryDto> expiredInventory(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Batch numbers to include. Optional.")
            List<String> batchNumbers,

            @ToolParam(description = "Expiration date range containing exactly two dates. Optional.")
            List<LocalDate> expiryDateRange,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.expiredInventory(
                ExpiredInventoryQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productBrands(productBrands)
                        .batchNumbers(batchNumbers)
                        .expiryDateRange(expiryDateRange)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_inventory_by_batch",
        description = """
            Get inventory at batch level.

            Use this when the user asks about inventory quantities, values,
            or details for specific batches.
            """
    )
    public List<InventoryByBatchDto> inventoryByBatch(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Batch numbers to include. Optional.")
            List<String> batchNumbers,

            @ToolParam(description = "Expiration date range containing exactly two dates. Optional.")
            List<LocalDate> expiryDateRange,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventoryByBatch(
                InventoryByBatchQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productBrands(productBrands)
                        .batchNumbers(batchNumbers)
                        .expiryDateRange(expiryDateRange)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_slow_moving_inventory",
        description = """
            Get inventory that is moving slowly based on stock movement over
            a specified period.

            Use this when the user asks which products are slow moving or
            have low inventory movement.
            """
    )
    public List<SlowMovingInventoryDto> slowMovingInventory(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Date-time range containing exactly two values. Optional.")
            List<LocalDateTime> createdAtRange,

            @ToolParam(description = "Number of days without sufficient movement to consider inventory slow moving. Optional.")
            Integer inactivityDays,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.slowMovingInventory(
                SlowMovingInventoryQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productBrands(productBrands)
                        .createdAtRange(createdAtRange)
                        .inactivityDays(inactivityDays)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_dead_stock_inventory",
        description = """
            Get inventory with no meaningful movement during the specified
            period.

            Use this when the user asks about dead stock, stagnant inventory,
            or products that have not moved.
            """
    )
    public List<DeadStockInventoryDto> deadStockInventory(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Date-time range containing exactly two values. Optional.")
            List<LocalDateTime> createdAtRange,

            @ToolParam(description = "Number of days without meaningful movement to consider inventory dead stock. Optional.")
            Integer inactivityDays,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.deadStockInventory(
                DeadStockInventoryQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productBrands(productBrands)
                        .createdAtRange(createdAtRange)
                        .inactivityDays(inactivityDays)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_inventory_days_of_supply",
        description = """
            Calculate estimated inventory days of supply.

            Use this when the user asks how many days current inventory will
            last based on historical inventory consumption or movement.
            """
    )
    public List<InventoryDaysOfSupplyDto> inventoryDaysOfSupply(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Date-time range containing exactly two values for the consumption lookback period. Optional.")
            List<LocalDateTime> createdAtRange,

            @ToolParam(description = "Number of historical days to use when calculating supply coverage. Optional.")
            Integer lookbackDays,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventoryDaysOfSupply(
                InventoryDaysOfSupplyQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productBrands(productBrands)
                        .createdAtRange(createdAtRange)
                        .lookbackDays(lookbackDays)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_inventory_turnover",
        description = """
            Calculate inventory turnover for the specified inventory and
            period.

            Use this when the user asks how quickly inventory is being sold,
            stock turnover, or inventory turnover performance.
            """
    )
    public List<InventoryTurnoverDto> inventoryTurnover(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Product categories to include. Optional.")
            List<String> productCategories,

            @ToolParam(description = "Product brands to include. Optional.")
            List<String> productBrands,

            @ToolParam(description = "Date-time range containing exactly two values. Optional.")
            List<LocalDateTime> createdAtRange,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventoryTurnover(
                InventoryTurnoverQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .productCategories(productCategories)
                        .productBrands(productBrands)
                        .createdAtRange(createdAtRange)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_inventory_balance_reconciliation",
        description = """
            Reconcile current inventory against inventory derived from stock
            movements.

            Use this when the user asks about inventory discrepancies,
            inventory reconciliation, or whether stock movement records agree
            with the inventory balance.
            """
    )
    public List<InventoryBalanceReconciliationDto> inventoryBalanceReconciliation(
            @ToolParam(description = "Store IDs to include. Optional.")
            List<UUID> storeIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Date-time range containing exactly two values. Optional.")
            List<LocalDateTime> createdAtRange,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventoryBalanceReconciliation(InvBalanceRecQueryParams.builder()
                        .storeIds(storeIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .createdAtRange(createdAtRange)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    @Tool(
        name = "get_inventory_in_transit",
        description = """
            Get inventory currently in transit between stores.

            Use this when the user asks what inventory has been sent but not
            fully received, what stock is being transferred, or what inventory
            is currently in transit.

            The report can be filtered by source store, destination store,
            product, product code, batch, transfer status, and sent date.
            """
    )
    public List<InventoryInTransitDto> inventoryInTransit(
            @ToolParam(description = "Source store IDs to include. Optional.")
            List<UUID> sourceStoreIds,

            @ToolParam(description = "Destination store IDs to include. Optional.")
            List<UUID> destinationStoreIds,

            @ToolParam(description = "Product IDs to include. Optional.")
            List<UUID> productIds,

            @ToolParam(description = "Product codes to include. Optional.")
            List<String> productCodes,

            @ToolParam(description = "Batch numbers to include. Optional.")
            List<String> batchNumbers,

            @ToolParam(description = "Stock transfer statuses to include. Optional.")
            List<StockTransferStatus> transferStatuses,

            @ToolParam(description = "Date-time range containing exactly two values for when transfers were sent. Optional.")
            List<LocalDateTime> sentAtRange,

            @ToolParam(description = "Number of records to skip. Optional.")
            Integer offset,

            @ToolParam(description = "Maximum number of records to return. Optional.")
            Integer limit) {

        return inventoryReportService.inventoryInTransit(
                InventoryInTransitQueryParams.builder()
                        .sourceStoreIds(sourceStoreIds)
                        .destinationStoreIds(destinationStoreIds)
                        .productIds(productIds)
                        .productCodes(productCodes)
                        .batchNumbers(batchNumbers)
                        .transferStatuses(transferStatuses)
                        .sentAtRange(sentAtRange)
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }
}