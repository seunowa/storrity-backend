/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.controller;


import com.storrity.storrity.sales.report.dto.AverageBasketDto;
import com.storrity.storrity.sales.report.dto.DailySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.HourlySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.MonthlySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.QuarterlySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.SalesByBrandDto;
import com.storrity.storrity.sales.report.dto.SalesByCashierDto;
import com.storrity.storrity.sales.report.dto.SalesByCategoryDto;
import com.storrity.storrity.sales.report.dto.SalesByClientSystemDto;
import com.storrity.storrity.sales.report.dto.SalesByCustomerDto;
import com.storrity.storrity.sales.report.dto.SalesByHourDto;
import com.storrity.storrity.sales.report.dto.SalesByMonthDto;
import com.storrity.storrity.sales.report.dto.SalesByProductDto;
import com.storrity.storrity.sales.report.dto.SalesByStoreDto;
import com.storrity.storrity.sales.report.dto.SalesByWeekdayDto;
import com.storrity.storrity.sales.report.dto.SalesReportQueryParams;
import com.storrity.storrity.sales.report.dto.WeeklySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.YearlySalesSummaryDto;
import com.storrity.storrity.sales.report.service.SalesReportService;
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
@RequestMapping("/api/v1/reports/sales")
@Tag(name = "Sales Reports", description = "Operations related to sales reporting")
public class SalesReportController {
private final SalesReportService salesReportService;

    @Autowired
    public SalesReportController(SalesReportService salesReportService) {
        this.salesReportService = salesReportService;
    }
    
    @Operation(
            operationId = "hourlySalesSummary",
            summary = "Hourly Sales Summary",
            description = "Returns aggregated sales statistics grouped by hour of the day. "
                    + "Supports filtering by date range, store, product, customer, "
                    + "cashier, client system and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Hourly sales summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = HourlySalesSummaryDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("hourly_summary")
    public List<HourlySalesSummaryDto> hourlySalesSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.hourlySalesSummary(params);
    }

    @Operation(
            operationId = "dailySalesSummary",
            summary = "Daily Sales Summary",
            description = "Returns aggregated sales statistics grouped by day. "
                    + "Supports filtering by date range, store, product, customer, "
                    + "cashier, client system and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Daily sales summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = DailySalesSummaryDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("daily_summary")
    public List<DailySalesSummaryDto> dailySalesSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.dailySalesSummary(params);
    }

    @Operation(
            operationId = "weeklySalesSummary",
            summary = "Weekly Sales Summary",
            description = "Returns aggregated sales statistics grouped by week. "
                    + "Supports filtering by date range, store, product, customer, "
                    + "cashier, client system and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Weekly sales summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = WeeklySalesSummaryDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("weekly_summary")
    public List<WeeklySalesSummaryDto> weeklySalesSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.weeklySalesSummary(params);
    }

    @Operation(
            operationId = "monthlySalesSummary",
            summary = "Monthly Sales Summary",
            description = "Returns aggregated sales statistics grouped by month. "
                    + "Supports filtering by date range, store, product, customer, "
                    + "cashier, client system and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Monthly sales summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = MonthlySalesSummaryDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("monthly_summary")
    public List<MonthlySalesSummaryDto> monthlySalesSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.monthlySalesSummary(params);
    }

    @Operation(
            operationId = "quarterlySalesSummary",
            summary = "Quarterly Sales Summary",
            description = "Returns aggregated sales statistics grouped by quarterly. "
                    + "Supports filtering by date range, store, product, customer, "
                    + "cashier, client system and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Quarterly sales summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = QuarterlySalesSummaryDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("quarterly_summary")
    public List<QuarterlySalesSummaryDto> quarterlySalesSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.quarterlySalesSummary(params);
    }

    @Operation(
            operationId = "yearlySalesSummary",
            summary = "Yearly Sales Summary",
            description = "Returns aggregated sales statistics grouped by yearly. "
                    + "Supports filtering by date range, store, product, customer, "
                    + "cashier, client system and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Yearly sales summary retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = YearlySalesSummaryDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("yearly_summary")
    public List<YearlySalesSummaryDto> yearlySalesSummary(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.yearlySalesSummary(params);
    }

    @Operation(
        operationId = "salesByStore",
        summary = "Sales by Store",
        description = "Returns aggregated sales statistics grouped by store. "
                + "Supports filtering by date range, store, product, customer, "
                + "cashier, client system and other report query parameters.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Sales by store retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = SalesByStoreDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("sales_by_store")
    public List<SalesByStoreDto> salesByStore(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.salesByStore(params);
    }

    @Operation(
        operationId = "SalesByProduct",
        summary = "Sales by Product",
        description = "Returns aggregated sales statistics grouped by product. "
                + "Supports filtering by date range, store, product, customer, "
                + "cashier, client system and other report query parameters.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Sales by product retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = SalesByProductDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("sales_by_product")
    public List<SalesByProductDto> salesByProduct(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.salesByProduct(params);
    }

    @Operation(
        operationId = "salesByCategory",
        summary = "Sales by Category",
        description = "Returns aggregated sales statistics grouped by category. "
                + "Supports filtering by date range, store, product, customer, "
                + "cashier, client system and other report query parameters.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Sales by category retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = SalesByCategoryDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("sales_by_category")
    public List<SalesByCategoryDto> salesByCategory(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.salesByCategory(params);
    }

    @Operation(
        operationId = "salesByBrand",
        summary = "Sales by Brand",
        description = "Returns aggregated sales statistics grouped by brand. "
                + "Supports filtering by date range, store, product, customer, "
                + "cashier, client system and other report query parameters.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Sales by brand retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = SalesByBrandDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("sales_by_brand")
    public List<SalesByBrandDto> salesByBrand(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.salesByBrand(params);
    }

    @Operation(
        operationId = "salesByCashier",
        summary = "Sales by Cashier",
        description = "Returns aggregated sales statistics grouped by cashier. "
                + "Supports filtering by date range, store, product, customer, "
                + "cashier, client system and other report query parameters.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Sales by cashier retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = SalesByCashierDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("sales_by_cashier")
    public List<SalesByCashierDto> salesByCashier(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.salesByCashier(params);
    }

    @Operation(
        operationId = "salesByClientSystem",
        summary = "Sales by Client System",
        description = "Returns aggregated sales statistics grouped by client system. "
                + "Supports filtering by date range, store, product, customer, "
                + "cashier, client system and other report query parameters.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Sales by client system retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = SalesByClientSystemDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("sales_by_client_system")
    public List<SalesByClientSystemDto> salesByClientSystem(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.salesByClientSystem(params);
    }
    
    
    @Operation(
        operationId = "salesByCustomer",
        summary = "Sales by Customer",
        description = "Returns aggregated sales statistics grouped by customer. "
                + "Supports filtering by date range, store, product, customer, "
                + "cashier, client system and other report query parameters.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Sales by customer retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = SalesByCustomerDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("sales_by_customer")
    public List<SalesByCustomerDto> salesByCustomer(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.salesByCustomer(params);
    }
    
    @Operation(
            operationId = "salesByMonth",
            summary = "Sales by Month",
            description = "Returns aggregated sales statistics grouped by month. "
                    + "Supports filtering by date range, store, product, customer, "
                    + "cashier, client system and other report query parameters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Sales by month retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = SalesByMonthDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("sales_by_month")
    public List<SalesByMonthDto> salesByMonth(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.salesByMonth(params);
    }    
    
    
    @Operation(
        operationId = "salesByHour",
        summary = "Sales by Hour",
        description = "Returns aggregated sales statistics grouped by hour. "
                + "Supports filtering by date range, store, product, customer, "
                + "cashier, client system and other report query parameters.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Sales by hour retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = SalesByHourDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("sales_by_hour")
    public List<SalesByHourDto> salesByHour(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.salesByHour(params);
    }
    
    
    @Operation(
        operationId = "salesByWeekday",
        summary = "Sales by Weekday",
        description = "Returns aggregated sales statistics grouped by weekday. "
                + "Supports filtering by date range, store, product, customer, "
                + "cashier, client system and other report query parameters.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Sales by weekday retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = SalesByWeekdayDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("sales_by_weekday")
    public List<SalesByWeekdayDto> salesByWeekday(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.salesByWeekday(params);
    }
    
    
    @Operation(
        operationId = "averageBasket",
        summary = "Average Basket",
        description = "Returns aggregated sales statistics grouped by average basket. "
                + "Supports filtering by date range, store, product, customer, "
                + "cashier, client system and other report query parameters.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Average basket retrieved successfully",
                content = @Content(
                        array = @ArraySchema(
                                schema = @Schema(implementation = AverageBasketDto.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected Error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("average_basket")
    public AverageBasketDto averageBasket(
            @ModelAttribute
            @Valid
            @ParameterObject
            SalesReportQueryParams params) {

        return salesReportService.averageBasket(params);
    }
}