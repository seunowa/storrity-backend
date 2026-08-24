/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 */
package com.storrity.storrity.stocktransfer.controller;

import com.storrity.storrity.stocktransfer.entity.StockTransferProcess;
import com.storrity.storrity.stocktransfer.entity.StockTransferProcessTemplate;
import com.storrity.storrity.stocktransfer.service.StockTransferProcessSettingsService;
import com.storrity.storrity.util.exception.AuthorizationError;
import com.storrity.storrity.util.exception.ServerError;
import com.storrity.storrity.util.exception.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/stock_transfer/settings")
@Tag(
    name = "Stock Transfer Process Settings",
    description = "Operations related to system-wide stock transfer process settings"
)
public class StockTransferProcessSettingsController {

    private final StockTransferProcessSettingsService
            stockTransferProcessSettingsService;

    @Autowired
    public StockTransferProcessSettingsController(
            StockTransferProcessSettingsService
                    stockTransferProcessSettingsService) {

        this.stockTransferProcessSettingsService =
                stockTransferProcessSettingsService;
    }

    @Operation(
        operationId = "getStockTransferProcessSettings",
        description = "Get the current system-wide stock transfer process settings",
        summary = "Get stock transfer process settings",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer process settings retrieved successfully",
            content = @Content(
                schema = @Schema(
                    implementation = StockTransferProcess.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Stock transfer process settings have not been configured"
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
    @GetMapping
    public ResponseEntity<StockTransferProcess>
            getStockTransferProcessSettings() {

        StockTransferProcess stockTransferProcess =
                stockTransferProcessSettingsService
                        .getStockTransferProcessSettings();

        if (stockTransferProcess == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(stockTransferProcess);
    }


    @Operation(
        operationId = "updateStockTransferProcessSettings",
        description = """
                      Replace the existing system-wide stock transfer
                      process settings with the supplied configuration
                      """,
        summary = "Update stock transfer process settings",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer process settings updated successfully",
            content = @Content(
                schema = @Schema(
                    implementation = StockTransferProcess.class
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
    @PutMapping
    public StockTransferProcess updateStockTransferProcessSettings(
            @RequestBody StockTransferProcess stockTransferProcess) {

        return stockTransferProcessSettingsService
                .updateStockTransferProcessSettings(stockTransferProcess);
    }


    @Operation(
        operationId = "getStockTransferProcessTemplates",
        description = "Get the available predefined stock transfer process templates",
        summary = "Get stock transfer process templates",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer process templates retrieved successfully",
            content = @Content(
                schema = @Schema(
                    implementation = StockTransferProcessTemplate.class
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
    @GetMapping("/templates")
    public StockTransferProcessTemplate getTemplates() {

        return stockTransferProcessSettingsService.getTemplates();
    }
}