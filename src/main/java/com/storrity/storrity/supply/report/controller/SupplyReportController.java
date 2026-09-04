package com.storrity.storrity.supply.report.controller;

import com.storrity.storrity.supply.report.dto.DeliveryVarianceByProductDto;
import com.storrity.storrity.supply.report.dto.DeliveryVarianceBySupplierDto;
import com.storrity.storrity.supply.report.dto.DeliveryVarianceDto;
import com.storrity.storrity.supply.report.dto.ProductProcurementSummaryDto;
import com.storrity.storrity.supply.report.dto.SupplierLeadTimeDto;
import com.storrity.storrity.supply.report.dto.SupplierPerformanceDto;
import com.storrity.storrity.supply.report.dto.SupplyReportQueryParams;
import com.storrity.storrity.supply.report.dto.SupplyStatusSummaryDto;
import com.storrity.storrity.supply.report.service.SupplyReportService;
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
 * REST endpoints for supply/procurement reports.
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/supply_reports")
@Tag(name = "Supply Reports", description = "Operations related to supply and procurement reporting")
public class SupplyReportController {

    private final SupplyReportService supplyReportService;

    @Autowired
    public SupplyReportController(SupplyReportService supplyReportService) {
        this.supplyReportService = supplyReportService;
    }

    @Operation(
            operationId = "getSupplyStatusSummary",
            description = "Get supplies grouped by supply status with their count and total value.",
            summary = "Get supply status summary",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supply status summary retrieved successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = SupplyStatusSummaryDto.class)))),
        @ApiResponse(responseCode = "400", description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403", description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping("/status_summary")
    public List<SupplyStatusSummaryDto> supplyStatusSummary(
            @ModelAttribute @Valid @ParameterObject SupplyReportQueryParams params) {
        return supplyReportService.supplyStatusSummary(params);
    }

    @Operation(
            operationId = "getDeliveryVariance",
            description = "Get delivery variance for supply items by comparing ordered and received quantities.",
            summary = "Get delivery variance",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Delivery variance retrieved successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeliveryVarianceDto.class)))),
        @ApiResponse(responseCode = "400", description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403", description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping("/delivery_variance")
    public List<DeliveryVarianceDto> deliveryVariance(
            @ModelAttribute @Valid @ParameterObject SupplyReportQueryParams params) {
        return supplyReportService.deliveryVariance(params);
    }

    @Operation(
            operationId = "getSupplierPerformance",
            description = "Get supplier delivery performance including on-time delivery rate, delays and spend.",
            summary = "Get supplier performance",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier performance retrieved successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = SupplierPerformanceDto.class)))),
        @ApiResponse(responseCode = "400", description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403", description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping("/supplier_performance")
    public List<SupplierPerformanceDto> supplierPerformance(
            @ModelAttribute @Valid @ParameterObject SupplyReportQueryParams params) {
        return supplyReportService.supplierPerformance(params);
    }

    @Operation(
            operationId = "getDeliveryVarianceByProduct",
            description = "Get delivery variance aggregated by product.",
            summary = "Get delivery variance by product",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Delivery variance by product retrieved successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeliveryVarianceByProductDto.class)))),
        @ApiResponse(responseCode = "400", description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403", description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping("/delivery_variance/by_product")
    public List<DeliveryVarianceByProductDto> deliveryVarianceByProduct(
            @ModelAttribute @Valid @ParameterObject SupplyReportQueryParams params) {
        return supplyReportService.deliveryVarianceByProduct(params);
    }

    @Operation(
            operationId = "getDeliveryVarianceBySupplier",
            description = "Get delivery variance aggregated by supplier.",
            summary = "Get delivery variance by supplier",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Delivery variance by supplier retrieved successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeliveryVarianceBySupplierDto.class)))),
        @ApiResponse(responseCode = "400", description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403", description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping("/delivery-variance/by-supplier")
    public List<DeliveryVarianceBySupplierDto> deliveryVarianceBySupplier(
            @ModelAttribute @Valid @ParameterObject SupplyReportQueryParams params) {
        return supplyReportService.deliveryVarianceBySupplier(params);
    }

    @Operation(
            operationId = "getSupplierLeadTime",
            description = "Get order-to-receipt lead-time statistics grouped by supplier.",
            summary = "Get supplier lead time",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier lead time retrieved successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = SupplierLeadTimeDto.class)))),
        @ApiResponse(responseCode = "400", description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403", description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping("/supplier_lead_time")
    public List<SupplierLeadTimeDto> supplierLeadTime(
            @ModelAttribute @Valid @ParameterObject SupplyReportQueryParams params) {
        return supplyReportService.supplierLeadTime(params);
    }

    @Operation(
            operationId = "getProductProcurementSummary",
            description = "Get procurement summary including quantities, spend, purchase prices and supplier counts.",
            summary = "Get product procurement summary",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product procurement summary retrieved successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductProcurementSummaryDto.class)))),
        @ApiResponse(responseCode = "400", description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403", description = "Authentication Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping("/product_procurement_summary")
    public List<ProductProcurementSummaryDto> productProcurementSummary(
            @ModelAttribute @Valid @ParameterObject SupplyReportQueryParams params) {
        return supplyReportService.productProcurementSummary(params);
    }
}
