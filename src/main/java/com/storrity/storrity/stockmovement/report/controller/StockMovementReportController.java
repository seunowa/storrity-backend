/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 * to edit this template
 */
package com.storrity.storrity.stockmovement.report.controller;

import com.storrity.storrity.stockmovement.report.dto.*;
import com.storrity.storrity.stockmovement.report.service.StockMovementReportService;
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
@RequestMapping("/api/v1/reports/stock_movements")
@Tag(
        name = "Stock Movement Reports",
        description = "Operations related to stock movement reporting"
)
public class StockMovementReportController {

    private final StockMovementReportService stockMovementReportService;

    @Autowired
    public StockMovementReportController(
            StockMovementReportService stockMovementReportService) {

        this.stockMovementReportService = stockMovementReportService;
    }

    @Operation(
            operationId = "hourlyStockMovementSummary",
            summary = "Hourly Stock Movement Summary",
            description = "Returns aggregated stock movement statistics grouped by hour. "
                    + "Supports filtering by date range, store, product, category, "
                    + "brand, movement type, direction and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Hourly stock movement summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = HourlyStockMovementSummaryDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("hourly_summary")
    public List<HourlyStockMovementSummaryDto> hourlyStockMovementSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.hourlyStockMovementSummary(params);
    }

    @Operation(
            operationId = "dailyStockMovementSummary",
            summary = "Daily Stock Movement Summary",
            description = "Returns aggregated stock movement statistics grouped by day. "
                    + "Supports filtering by date range, store, product, category, "
                    + "brand, movement type, direction and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Daily stock movement summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = DailyStockMovementSummaryDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("daily_summary")
    public List<DailyStockMovementSummaryDto> dailyStockMovementSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.dailyStockMovementSummary(params);
    }

    @Operation(
            operationId = "weeklyStockMovementSummary",
            summary = "Weekly Stock Movement Summary",
            description = "Returns aggregated stock movement statistics grouped by week. "
                    + "Supports filtering by date range, store, product, category, "
                    + "brand, movement type, direction and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Weekly stock movement summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = WeeklyStockMovementSummaryDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("weekly_summary")
    public List<WeeklyStockMovementSummaryDto> weeklyStockMovementSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.weeklyStockMovementSummary(params);
    }

    @Operation(
            operationId = "monthlyStockMovementSummary",
            summary = "Monthly Stock Movement Summary",
            description = "Returns aggregated stock movement statistics grouped by month. "
                    + "Supports filtering by date range, store, product, category, "
                    + "brand, movement type, direction and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Monthly stock movement summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = MonthlyStockMovementSummaryDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("monthly_summary")
    public List<MonthlyStockMovementSummaryDto> monthlyStockMovementSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.monthlyStockMovementSummary(params);
    }

    @Operation(
            operationId = "quarterlyStockMovementSummary",
            summary = "Quarterly Stock Movement Summary",
            description = "Returns aggregated stock movement statistics grouped by quarter. "
                    + "Supports filtering by date range, store, product, category, "
                    + "brand, movement type, direction and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Quarterly stock movement summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = QuarterlyStockMovementSummaryDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("quarterly_summary")
    public List<QuarterlyStockMovementSummaryDto> quarterlyStockMovementSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.quarterlyStockMovementSummary(params);
    }

    @Operation(
            operationId = "yearlyStockMovementSummary",
            summary = "Yearly Stock Movement Summary",
            description = "Returns aggregated stock movement statistics grouped by year. "
                    + "Supports filtering by date range, store, product, category, "
                    + "brand, movement type, direction and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Yearly stock movement summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = YearlyStockMovementSummaryDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("yearly_summary")
    public List<YearlyStockMovementSummaryDto> yearlyStockMovementSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.yearlyStockMovementSummary(params);
    }

    @Operation(
            operationId = "stockMovementsByStore",
            summary = "Stock Movements by Store",
            description = "Returns aggregated stock movement statistics grouped by store. "
                    + "Supports filtering by date range, store, product, category, "
                    + "brand, movement type, direction and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Stock movements by store retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = StockMovementsByStoreDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("stock_movements_by_store")
    public List<StockMovementsByStoreDto> stockMovementsByStore(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.stockMovementsByStore(params);
    }

    @Operation(
            operationId = "stockMovementsByProductId",
            summary = "Stock Movements by Product id",
            description = "Returns aggregated stock movement statistics grouped by product id. "
                    + "Supports filtering by date range, store, product, category, "
                    + "brand, movement type, direction and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Stock movements by product id retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = StockMovementsByProductIdDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("stock_movements_by_product_id")
    public List<StockMovementsByProductIdDto> stockMovementsByProduct(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.stockMovementsByProductId(params);
    }

    @Operation(
            operationId = "stockMovementsByProductCode",
            summary = "Stock Movements by Product Code",
            description = "Returns aggregated stock movement statistics grouped by product code. "
                    + "Supports filtering by date range, store, product, category, "
                    + "brand, movement type, direction and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Stock movements by product code retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = StockMovementsByProductIdDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("stock_movements_by_product_code")
    public List<StockMovementsByProductIdDto> stockMovementsByProductCode(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.stockMovementsByProductCode(params);
    }

    @Operation(
            operationId = "stockMovementsByCategory",
            summary = "Stock Movements by Category",
            description = "Returns aggregated stock movement statistics grouped by "
                    + "product category. Supports filtering by date range, store, "
                    + "product, category, brand, movement type, direction and other "
                    + "report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Stock movements by category retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = StockMovementsByCategoryDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("stock_movements_by_category")
    public List<StockMovementsByCategoryDto> stockMovementsByCategory(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.stockMovementsByCategory(params);
    }

    @Operation(
            operationId = "stockMovementsByBrand",
            summary = "Stock Movements by Brand",
            description = "Returns aggregated stock movement statistics grouped by "
                    + "product brand. Supports filtering by date range, store, "
                    + "product, category, brand, movement type, direction and other "
                    + "report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Stock movements by brand retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = StockMovementsByBrandDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("stock_movements_by_brand")
    public List<StockMovementsByBrandDto> stockMovementsByBrand(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.stockMovementsByBrand(params);
    }

    @Operation(
            operationId = "stockMovementsByMovementType",
            summary = "Stock Movements by Movement Type",
            description = "Returns aggregated stock movement statistics grouped by "
                    + "movement type. Supports filtering by date range, store, product, "
                    + "category, brand, movement type, direction and other report "
                    + "query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Stock movements by movement type retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = StockMovementsByMovementTypeDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("stock_movements_by_movement_type")
    public List<StockMovementsByMovementTypeDto> stockMovementsByMovementType(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.stockMovementsByMovementType(params);
    }

    @Operation(
            operationId = "stockMovementsByDirection",
            summary = "Stock Movements by Direction",
            description = "Returns aggregated stock movement statistics grouped by "
                    + "movement direction. Supports filtering by date range, store, "
                    + "product, category, brand, movement type, direction and other "
                    + "report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Stock movements by direction retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(
                                        implementation = StockMovementsByDirectionDto.class
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
                description = "Unexpected Error",
                content = @Content(
                        schema = @Schema(
                                implementation = ServerError.class
                        )
                )
        )
    })
    @GetMapping("stock_movements_by_direction")
    public List<StockMovementsByDirectionDto> stockMovementsByDirection(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockMovementReportQueryParams params) {

        return stockMovementReportService.stockMovementsByDirection(params);
    }
}