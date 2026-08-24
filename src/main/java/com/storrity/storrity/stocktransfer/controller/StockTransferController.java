/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 */
package com.storrity.storrity.stocktransfer.controller;

import com.storrity.storrity.stocktransfer.dto.StockTransferCreationDto;
import com.storrity.storrity.stocktransfer.dto.StockTransferDto;
import com.storrity.storrity.stocktransfer.dto.StockTransferQueryParams;
import com.storrity.storrity.stocktransfer.dto.StockTransferReceiveDto;
import com.storrity.storrity.stocktransfer.service.StockTransferService;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ApiError;
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
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/stock_transfers")
@Tag(
    name = "Stock Transfers",
    description = "Operations related to inventory stock transfer management"
)
public class StockTransferController {

    private final StockTransferService stockTransferService;

    @Autowired
    public StockTransferController(
            StockTransferService stockTransferService) {

        this.stockTransferService = stockTransferService;
    }

    /*
     * ============================================================
     * QUERY OPERATIONS
     * ============================================================
     */

    @Operation(
        operationId = "getStockTransfer",
        summary = "Get a stock transfer by ID",
        description = "Retrieves a stock transfer and its current workflow state",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer retrieved successfully",
            content = @Content(
                schema = @Schema(implementation = StockTransferDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Stock transfer not found",
            content = @Content(
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(implementation = AuthorizationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(implementation = ServerError.class)
            )
        )
    })
    @GetMapping("/{id}")
    public StockTransferDto fetch(
            @PathVariable("id") UUID id) {

        return stockTransferService.fetch(id);
    }


    @Operation(
        operationId = "listStockTransfers",
        summary = "List stock transfers",
        description = "Returns stock transfers matching the supplied query parameters",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfers retrieved successfully",
            content = @Content(
                array = @ArraySchema(
                    schema = @Schema(implementation = StockTransferDto.class)
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(implementation = AuthorizationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(implementation = ServerError.class)
            )
        )
    })
    @GetMapping
    public List<StockTransferDto> list(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockTransferQueryParams params) {

        return stockTransferService.list(params);
    }


    @Operation(
        operationId = "countStockTransfers",
        summary = "Count stock transfers",
        description = "Returns the number of stock transfers matching the supplied query parameters",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfers counted successfully",
            content = @Content(
                schema = @Schema(implementation = CountDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(implementation = AuthorizationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(implementation = ServerError.class)
            )
        )
    })
    @GetMapping("/count")
    public CountDto count(
            @ModelAttribute
            @Valid
            @ParameterObject
            StockTransferQueryParams params) {

        return stockTransferService.count(params);
    }


    /*
     * ============================================================
     * DRAFT
     * ============================================================
     */

    @Operation(
        operationId = "createStockTransferDraft",
        summary = "Create stock transfer draft",
        description = """
                      Creates a new stock transfer in DRAFT state.
                      Products are identified by product code and resolved
                      against the source store.
                      """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer draft created successfully",
            content = @Content(
                schema = @Schema(implementation = StockTransferDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(implementation = AuthorizationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(implementation = ServerError.class)
            )
        )
    })
    @PostMapping("/draft")
    public StockTransferDto createDraft(
            @RequestBody
            @Valid
            StockTransferCreationDto dto) {

        return stockTransferService.createDraft(dto);
    }


    @Operation(
        operationId = "updateStockTransferDraft",
        summary = "Update stock transfer draft",
        description = """
                      Updates an existing stock transfer while it is still
                      in the DRAFT stage.
                      """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer draft updated successfully",
            content = @Content(
                schema = @Schema(implementation = StockTransferDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error or invalid workflow transition",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Stock transfer not found",
            content = @Content(
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(implementation = AuthorizationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(implementation = ServerError.class)
            )
        )
    })
    @PatchMapping("/{id}/draft")
    public StockTransferDto updateDraft(
            @PathVariable("id") UUID id,
            @RequestBody
            @Valid
            StockTransferCreationDto dto) {

        return stockTransferService.updateDraft(id, dto);
    }


    /*
     * ============================================================
     * DRAFT WORKFLOW
     * ============================================================
     */

    @Operation(
        operationId = "submitStockTransferDraft",
        summary = "Submit stock transfer draft",
        description = "Submits a stock transfer draft for approval",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer draft submitted successfully",
            content = @Content(
                schema = @Schema(implementation = StockTransferDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid workflow transition",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Stock transfer not found",
            content = @Content(
                schema = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PostMapping("/{id}/submit_draft")
    public StockTransferDto submitDraft(
            @PathVariable("id") UUID id) {

        return stockTransferService.submitDraft(id);
    }


    @Operation(
        operationId = "approveStockTransferDraft",
        summary = "Approve stock transfer draft",
        description = "Approves a submitted stock transfer draft",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer draft approved successfully",
            content = @Content(
                schema = @Schema(implementation = StockTransferDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid workflow transition",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Stock transfer not found",
            content = @Content(
                schema = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PostMapping("/{id}/approve_draft")
    public StockTransferDto approveDraft(
            @PathVariable("id") UUID id) {

        return stockTransferService.approveDraft(id);
    }


    /*
     * ============================================================
     * SEND
     * ============================================================
     */

    @Operation(
        operationId = "sendStockTransfer",
        summary = "Send stock transfer",
        description = """
                      Sends the stock transfer from the source store.
                      This operation creates an OUTFLOW stock movement
                      for the transferred products.
                      """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer sent successfully",
            content = @Content(
                schema = @Schema(implementation = StockTransferDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error or invalid workflow transition",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Stock transfer not found",
            content = @Content(
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(implementation = AuthorizationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(implementation = ServerError.class)
            )
        )
    })
    @PostMapping("/{id}/send")
    public StockTransferDto send(
            @PathVariable("id") UUID id) {

        return stockTransferService.send(id);
    }


    /*
     * ============================================================
     * RECEIPT WORKFLOW
     * ============================================================
     */

    @Operation(
        operationId = "submitStockTransferReceipt",
        summary = "Submit stock transfer receipt",
        description = "Submits the stock transfer receipt for approval",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer receipt submitted successfully",
            content = @Content(
                schema = @Schema(implementation = StockTransferDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid workflow transition",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Stock transfer not found",
            content = @Content(
                schema = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PostMapping("/{id}/submit_receipt")
    public StockTransferDto submitReceipt(
            @PathVariable("id") UUID id) {

        return stockTransferService.submitReceipt(id);
    }


    @Operation(
        operationId = "approveStockTransferReceipt",
        summary = "Approve stock transfer receipt",
        description = "Approves the submitted stock transfer receipt",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer receipt approved successfully",
            content = @Content(
                schema = @Schema(implementation = StockTransferDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid workflow transition",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Stock transfer not found",
            content = @Content(
                schema = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PostMapping("/{id}/approve_receipt")
    public StockTransferDto approveReceipt(
            @PathVariable("id") UUID id) {

        return stockTransferService.approveReceipt(id);
    }


    /*
     * ============================================================
     * RECEIVE
     * ============================================================
     */

    @Operation(
        operationId = "receiveStockTransfer",
        summary = "Receive stock transfer",
        description = """
                      Receives the transferred stock into the destination
                      store. Products are resolved by product code against
                      the destination store and an INFLOW stock movement
                      is created.
                      """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer received successfully",
            content = @Content(
                schema = @Schema(implementation = StockTransferDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error or invalid workflow transition",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Stock transfer not found",
            content = @Content(
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(implementation = AuthorizationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(implementation = ServerError.class)
            )
        )
    })
    @PostMapping("/{id}/receive")
    public StockTransferDto receive(
            @PathVariable("id") UUID id,
            @RequestBody
            @Valid
            StockTransferReceiveDto dto) {

        return stockTransferService.receive(id, dto);
    }


    /*
     * ============================================================
     * CANCEL
     * ============================================================
     */

    @Operation(
        operationId = "cancelStockTransfer",
        summary = "Cancel stock transfer",
        description = """
                      Cancels a stock transfer that has not yet been received.
                      If stock has already been sent, the service reverses the
                      source-store stock movement.
                      """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer canceled successfully",
            content = @Content(
                schema = @Schema(implementation = StockTransferDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Stock transfer cannot be canceled",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Stock transfer not found",
            content = @Content(
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(implementation = AuthorizationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(implementation = ServerError.class)
            )
        )
    })
    @PostMapping("/{id}/cancel")
    public StockTransferDto cancel(
            @PathVariable("id") UUID id) {

        return stockTransferService.cancel(id);
    }


    /*
     * ============================================================
     * DELETE
     * ============================================================
     */

    @Operation(
        operationId = "deleteStockTransfer",
        summary = "Delete stock transfer",
        description = "Deletes a stock transfer while it is still eligible for deletion",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock transfer deleted successfully",
            content = @Content(
                schema = @Schema(implementation = StockTransferDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Stock transfer cannot be deleted",
            content = @Content(
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Stock transfer not found",
            content = @Content(
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(implementation = AuthorizationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                schema = @Schema(implementation = ServerError.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    public StockTransferDto delete(
            @PathVariable("id") UUID id) {

        return stockTransferService.delete(id);
    }
}