/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.inventory.report.controller;

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
import com.storrity.storrity.inventory.report.service.InventoryReportService;
import com.storrity.storrity.util.exception.AuthorizationError;
import com.storrity.storrity.util.exception.ServerError;
import com.storrity.storrity.util.exception.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/inventory/reports")
@Tag(
        name = "Inventory Reports",
        description = "Operations related to inventory reporting"
)
public class InventoryReportController {

    private final InventoryReportService inventoryReportService;

    @Autowired
    public InventoryReportController(
            InventoryReportService inventoryReportService) {
        this.inventoryReportService = inventoryReportService;
    }

    /*
     * -------------------------------------------------------------------------
     * Inventory Summary
     * -------------------------------------------------------------------------
     */

    @Operation(
            operationId = "getInventorySummary",
            description = "Get a summary of current inventory",
            summary = "Get inventory summary",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory summary retrieved successfully",
                content = @Content(
                        schema = @Schema(
                                implementation = InventorySummaryDto.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/summary")
    public InventorySummaryDto inventorySummary(
            @ModelAttribute @Valid @ParameterObject InventorySummaryQueryParams params) {

        return inventoryReportService.inventorySummary(params);
    }

    /*
     * -------------------------------------------------------------------------
     * Inventory by Product
     * -------------------------------------------------------------------------
     */

    @Operation(
            operationId = "listInventoryByProduct",
            description = "List current inventory grouped by product",
            summary = "List inventory by product",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory by product retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = InventoryByProductDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/by_product")
    public List<InventoryByProductDto> inventoryByProduct(
            @ModelAttribute @Valid @ParameterObject InventoryByProductQueryParams params) {

        return inventoryReportService.inventoryByProduct(params);
    }

    /*
     * -------------------------------------------------------------------------
     * Inventory by Store
     * -------------------------------------------------------------------------
     */

    @Operation(
            operationId = "listInventoryByStore",
            description = "List current inventory grouped by store",
            summary = "List inventory by store",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory by store retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = InventoryByStoreDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/by_store")
    public List<InventoryByStoreDto> inventoryByStore(
            @ModelAttribute @Valid @ParameterObject InventoryByStoreQueryParams params) {

        return inventoryReportService.inventoryByStore(params);
    }

    /*
     * -------------------------------------------------------------------------
     * Inventory by Category
     * -------------------------------------------------------------------------
     */

    @Operation(
            operationId = "listInventoryByCategory",
            description = "List current inventory grouped by category",
            summary = "List inventory by category",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory by category retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = InventoryByCategoryDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/by_category")
    public List<InventoryByCategoryDto> inventoryByCategory(
            @ModelAttribute @Valid @ParameterObject InventoryByCategoryQueryParams params) {

        return inventoryReportService.inventoryByCategory(params);
    }

    /*
     * -------------------------------------------------------------------------
     * Inventory by Brand
     * -------------------------------------------------------------------------
     */

    @Operation(
            operationId = "listInventoryByBrand",
            description = "List current inventory grouped by brand",
            summary = "List inventory by brand",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory by brand retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = InventoryByBrandDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/by_brand")
    public List<InventoryByBrandDto> inventoryByBrand(
            @ModelAttribute @Valid @ParameterObject InventoryByBrandQueryParams params) {

        return inventoryReportService.inventoryByBrand(params);
    }

    /*
     * -------------------------------------------------------------------------
     * Inventory by Product Type
     * -------------------------------------------------------------------------
     */

    @Operation(
            operationId = "listInventoryByProductType",
            description = "List current inventory grouped by product type",
            summary = "List inventory by product type",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory by product type retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = InventoryByProductTypeDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/by_product_type")
    public List<InventoryByProductTypeDto> inventoryByProductType(
            @ModelAttribute @Valid @ParameterObject InventoryByProductTypeQueryParams params) {

        return inventoryReportService.inventoryByProductType(params);
    }

    /*
     * -------------------------------------------------------------------------
     * Stock Level Reports
     * -------------------------------------------------------------------------
     */

    @Operation(
            operationId = "listLowStockInventory",
            description = "List products whose current stock is below the configured reorder level",
            summary = "List low stock inventory",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Low stock inventory retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = LowStockInventoryDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/low_stock")
    public List<LowStockInventoryDto> lowStockInventory(
            @ModelAttribute @Valid @ParameterObject LowStockInventoryQueryParams params) {

        return inventoryReportService.lowStockInventory(params);
    }

    @Operation(
            operationId = "listOutOfStockInventory",
            description = "List products with no stock currently available",
            summary = "List out of stock inventory",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Out of stock inventory retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = OutOfStockInventoryDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/out_of_stock")
    public List<OutOfStockInventoryDto> outOfStockInventory(
            @ModelAttribute @Valid @ParameterObject OutOfStockInventoryQueryParams params) {

        return inventoryReportService.outOfStockInventory(params);
    }

    @Operation(
            operationId = "listOverstockInventory",
            description = "List products whose current stock exceeds the configured maximum stock level",
            summary = "List overstock inventory",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Overstock inventory retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = OverstockInventoryDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/overstock")
    public List<OverstockInventoryDto> overstockInventory(
            @ModelAttribute @Valid @ParameterObject OverstockInventoryQueryParams params) {

        return inventoryReportService.overstockInventory(params);
    }

    @Operation(
            operationId = "listInventoryReorderRecommendations",
            description = "List products that require replenishment based on their current stock and configured reorder parameters",
            summary = "List inventory reorder recommendations",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory reorder recommendations retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = InventoryReorderRecommendationDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/reorder_recommendations")
    public List<InventoryReorderRecommendationDto> inventoryReorderRecommendations(
            @ModelAttribute @Valid @ParameterObject InvReorderRecQueryParams params) {

        return inventoryReportService.inventoryReorderRecommendations(params);
    }

    /*
     * -------------------------------------------------------------------------
     * Expiry / Batch Reports
     * -------------------------------------------------------------------------
     */

    @Operation(
            operationId = "listExpiringInventory",
            description = "List inventory with an expiry date within the specified date range",
            summary = "List expiring inventory",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Expiring inventory retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = ExpiringInventoryDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/expiring")
    public List<ExpiringInventoryDto> expiringInventory(
            @ModelAttribute @Valid @ParameterObject ExpiringInventoryQueryParams params) {

        return inventoryReportService.expiringInventory(params);
    }

    @Operation(
            operationId = "listExpiredInventory",
            description = "List inventory that has already expired",
            summary = "List expired inventory",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Expired inventory retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = ExpiredInventoryDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/expired")
    public List<ExpiredInventoryDto> expiredInventory(
            @ModelAttribute @Valid @ParameterObject ExpiredInventoryQueryParams params) {

        return inventoryReportService.expiredInventory(params);
    }

    @Operation(
            operationId = "listInventoryByBatch",
            description = "List current inventory grouped by batch",
            summary = "List inventory by batch",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory by batch retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = InventoryByBatchDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/by_batch")
    public List<InventoryByBatchDto> inventoryByBatch(
            @ModelAttribute @Valid @ParameterObject InventoryByBatchQueryParams params) {

        return inventoryReportService.inventoryByBatch(params);
    }

    /*
     * -------------------------------------------------------------------------
     * Inventory Movement / Efficiency Reports
     * -------------------------------------------------------------------------
     */

    @Operation(
            operationId = "listSlowMovingInventory",
            description = "List inventory with low movement activity over the specified period",
            summary = "List slow moving inventory",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Slow moving inventory retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = SlowMovingInventoryDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/slow_moving")
    public List<SlowMovingInventoryDto> slowMovingInventory(
            @ModelAttribute @Valid @ParameterObject SlowMovingInventoryQueryParams params) {

        return inventoryReportService.slowMovingInventory(params);
    }

    @Operation(
            operationId = "listDeadStockInventory",
            description = "List inventory with no outbound movement over the specified inactivity period",
            summary = "List dead stock inventory",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Dead stock inventory retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = DeadStockInventoryDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/dead_stock")
    public List<DeadStockInventoryDto> deadStockInventory(
            @ModelAttribute @Valid @ParameterObject DeadStockInventoryQueryParams params) {

        return inventoryReportService.deadStockInventory(params);
    }

    @Operation(
            operationId = "listInventoryDaysOfSupply",
            description = "Estimate the number of days the current inventory can support based on historical stock consumption",
            summary = "List inventory days of supply",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory days of supply retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = InventoryDaysOfSupplyDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/days_of_supply")
    public List<InventoryDaysOfSupplyDto> inventoryDaysOfSupply(
            @ModelAttribute @Valid @ParameterObject InventoryDaysOfSupplyQueryParams params) {

        return inventoryReportService.inventoryDaysOfSupply(params);
    }

    @Operation(
            operationId = "listInventoryTurnover",
            description = "Calculate inventory turnover over the specified period",
            summary = "List inventory turnover",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory turnover retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = InventoryTurnoverDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/turnover")
    public List<InventoryTurnoverDto> inventoryTurnover(
            @ModelAttribute @Valid @ParameterObject InventoryTurnoverQueryParams params) {

        return inventoryReportService.inventoryTurnover(params);
    }

    /*
     * -------------------------------------------------------------------------
     * Reconciliation
     * -------------------------------------------------------------------------
     */

    @Operation(
            operationId = "listInventoryBalanceReconciliation",
            description = "Compare the current inventory snapshot against the balance derived from stock movements",
            summary = "Reconcile inventory balances",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory balance reconciliation retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = InventoryBalanceReconciliationDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/balance_reconciliation")
    public List<InventoryBalanceReconciliationDto> inventoryBalanceReconciliation(
            @ModelAttribute @Valid @ParameterObject InvBalanceRecQueryParams params) {

        return inventoryReportService.inventoryBalanceReconciliation(params);
    }

    /*
     * -------------------------------------------------------------------------
     * Inventory in Transit
     * -------------------------------------------------------------------------
     */

    @Operation(
            operationId = "listInventoryInTransit",
            description = "List inventory that has been sent between stores but has not yet been received",
            summary = "List inventory in transit",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Inventory in transit retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = InventoryInTransitDto.class
                                )
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ValidationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(
                        schema = @Schema(
                                implementation = AuthorizationError.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("/in_transit")
    public List<InventoryInTransitDto> inventoryInTransit(
            @ModelAttribute @Valid @ParameterObject InventoryInTransitQueryParams params) {

        return inventoryReportService.inventoryInTransit(params);
    }
}