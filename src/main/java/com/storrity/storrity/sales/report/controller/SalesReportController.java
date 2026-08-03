/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.controller;


import com.storrity.storrity.sales.report.dto.DailySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.MonthlySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.QuarterlySalesSummaryDto;
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

}