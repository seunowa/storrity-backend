package com.storrity.storrity.cashier.controller;

import com.storrity.storrity.cashier.dto.*;
import com.storrity.storrity.cashier.entity.CashFlow;
import com.storrity.storrity.cashier.service.CashierShiftService;
import com.storrity.storrity.util.exception.ApiError;
import com.storrity.storrity.util.exception.AuthorizationError;
import com.storrity.storrity.util.exception.ServerError;
import com.storrity.storrity.util.exception.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/cashier_shifts")
@Tag(name = "Cashier Shifts", description = "Operations related to cashier shifts")
public class CashierShiftController {

    private final CashierShiftService service;

    public CashierShiftController(CashierShiftService s) {
        service = s;
    }

    @Operation(
            operationId = "openCashierShift",
            description = "Opens a new cashier shift "
                + "requires permission: CASHIER_SHIFT_CREATE",
            summary = "Open a cashier shift",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Cashier shift opened successfully",
            content = @Content(schema = @Schema(implementation = CashierShiftView.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CASHIER_SHIFT_CREATE')")
    public CashierShiftView open(@Valid @RequestBody OpenCashierShiftRequest r) {
        return service.openShift(r);
    }

    @Operation(
            operationId = "getCashierShift",
            description = "Get a cashier shift by id "
                + "requires permission: CASHIER_SHIFT_READ",
            summary = "Get a cashier shift by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cashier shift retrieved successfully",
            content = @Content(schema = @Schema(implementation = CashierShiftView.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cashier shift not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("/{shiftId}")
    @PreAuthorize("hasAuthority('CASHIER_SHIFT_READ')")
    public CashierShiftView get(
            @Parameter(description = "ID of the cashier shift") @PathVariable UUID shiftId) {
        return service.getShift(shiftId);
    }

    @Operation(
            operationId = "getExpectedAmounts",
            description = "Get the expected amount per payment method for a cashier shift "
                + "requires permission: CASHIER_SHIFT_READ",
            summary = "Get expected payment amounts for a shift",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Expected amounts retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = ExpectedPaymentAmount.class))
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cashier shift not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("/{shiftId}/expected_amounts")
    @PreAuthorize("hasAuthority('CASHIER_SHIFT_READ')")
    public List<ExpectedPaymentAmount> expected(
            @Parameter(description = "ID of the cashier shift") @PathVariable UUID shiftId) {
        return service.expectedAmounts(shiftId);
    }

    @Operation(
            operationId = "recordCashFlow",
            description = "Records a manual cash flow (cash in/out) against a cashier shift "
                + "requires permission: CASH_FLOW_CREATE",
            summary = "Record a manual cash flow",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Cash flow recorded successfully",
            content = @Content(schema = @Schema(implementation = CashFlow.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Cashier shift not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PostMapping("/{shiftId}/cash_flows")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CASH_FLOW_CREATE')")
    public CashFlow flow(
            @Parameter(description = "ID of the cashier shift") @PathVariable UUID shiftId,
            @Valid @RequestBody CashFlowRequest request) {
        return service.recordManualFlow(shiftId, request);
    }

    @Operation(
            operationId = "submitShiftClosing",
            description = "Submits the counted closing amounts for a cashier shift "
                + "requires permission: CASHIER_SUBMIT_SHIFT_CLOSE",
            summary = "Submit shift closing",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Shift closing submitted successfully",
            content = @Content(schema = @Schema(implementation = CashierShiftView.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Cashier shift not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PostMapping("/{shiftId}/closing")
    @PreAuthorize("hasAuthority('CASHIER_SUBMIT_SHIFT_CLOSE')")
    public CashierShiftView closing(
            @Parameter(description = "ID of the cashier shift") @PathVariable UUID shiftId,
            @Valid @RequestBody SubmitShiftClosingRequest r) {
        return service.submitClosing(shiftId, r);
    }

    @Operation(
            operationId = "closeCashierShift",
            description = "Closes a cashier shift "
                + "requires permission: CASHIER_SHIFT_CLOSE",
            summary = "Close a cashier shift",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cashier shift closed successfully",
            content = @Content(schema = @Schema(implementation = CashierShiftView.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cashier shift not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PostMapping("/{shiftId}/close")
    @PreAuthorize("hasAuthority('CASHIER_SHIFT_CLOSE')")
    public CashierShiftView close(
            @Parameter(description = "ID of the cashier shift") @PathVariable UUID shiftId) {
        return service.closeShift(shiftId);
    }

    @Operation(
            operationId = "rejectShiftClosing",
            description = "Rejects a submitted shift closing, with a reason "
                + "requires permission: CASHIER_SHIFT_REVIEW",
            summary = "Reject a shift closing",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Shift closing rejected successfully",
            content = @Content(schema = @Schema(implementation = CashierShiftView.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Cashier shift not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PostMapping("/{shiftId}/reject_closing")
    @PreAuthorize("hasAuthority('CASHIER_SHIFT_REVIEW')")
    public CashierShiftView reject(
            @Parameter(description = "ID of the cashier shift") @PathVariable UUID shiftId,
            @Parameter(description = "Reason for rejecting the closing", required = true)
            @RequestParam String reason) {
        return service.rejectClosing(shiftId, reason);
    }
}