/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.controller;

import com.storrity.storrity.supply.dto.DeliveryDto;
import com.storrity.storrity.supply.dto.PurchaseOrderCreationDto;
import com.storrity.storrity.supply.dto.SupplyDto;
import com.storrity.storrity.supply.dto.SupplyQueryParams;
import com.storrity.storrity.supply.service.SupplyService;
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
@RequestMapping("/api/v1/supplies")
@Tag(
    name = "Supplies",
    description = "Operations related to supply management"
)
public class SupplyController {

    private final SupplyService supplyService;

    @Autowired
    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    /*
     * ============================================================
     * QUERY OPERATIONS
     * ============================================================
     */

    @Operation(
        operationId = "getSupply",
        summary = "Get a supply by ID",
        description = "Retrieves a supply and its current workflow state",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Supply retrieved successfully",
            content = @Content(
                schema = @Schema(implementation = SupplyDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Supply not found",
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
    public SupplyDto fetch(
        @PathVariable("id") UUID id
    ) {
        return supplyService.fetch(id);
    }


    @Operation(
        operationId = "listSupplies",
        summary = "List supplies",
        description = "Returns supplies matching the supplied query parameters",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Supplies retrieved successfully",
            content = @Content(
                array = @ArraySchema(
                    schema = @Schema(implementation = SupplyDto.class)
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
    public List<SupplyDto> list(
        @ModelAttribute
        @Valid
        @ParameterObject
        SupplyQueryParams params
    ) {
        return supplyService.list(params);
    }


    @Operation(
        operationId = "countSupplies",
        summary = "Count supplies",
        description = "Returns the number of supplies matching the supplied query parameters",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Supplies counted successfully",
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
        SupplyQueryParams params
    ) {
        return supplyService.count(params);
    }


    /*
     * ============================================================
     * DRAFT
     * ============================================================
     */

    @Operation(
        operationId = "createSupplyDraft",
        summary = "Create supply draft",
        description = "Creates a new supply in DRAFT state",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Supply draft created successfully",
            content = @Content(
                schema = @Schema(implementation = SupplyDto.class)
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
    public SupplyDto createDraft(
        @RequestBody
        @Valid
        PurchaseOrderCreationDto dto
    ) {
        return supplyService.createDraft(dto);
    }


    @Operation(
        operationId = "updateSupplyDraft",
        summary = "Update supply draft",
        description = "Updates an existing supply while it is still in the DRAFT stage",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Supply draft updated successfully",
            content = @Content(
                schema = @Schema(implementation = SupplyDto.class)
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
            description = "Supply not found",
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
    public SupplyDto updateDraft(
        @PathVariable("id") UUID id,
        @RequestBody
        @Valid
        PurchaseOrderCreationDto dto
    ) {
        return supplyService.updateDraft(id, dto);
    }


    /*
     * ============================================================
     * DRAFT WORKFLOW
     * ============================================================
     */

    @Operation(
        operationId = "submitSupplyDraft",
        summary = "Submit supply draft",
        description = "Submits a draft for approval",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/{id}/submit_draft")
    public SupplyDto submitDraft(
        @PathVariable("id") UUID id
    ) {
        return supplyService.submitDraft(id);
    }


    @Operation(
        operationId = "approveSupplyDraft",
        summary = "Approve supply draft",
        description = "Approves a submitted supply draft",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/{id}/approve_draft")
    public SupplyDto approveDraft(
        @PathVariable("id") UUID id
    ) {
        return supplyService.approveDraft(id);
    }


    /*
     * ============================================================
     * ORDER
     * ============================================================
     */

    @Operation(
        operationId = "orderSupply",
        summary = "Order supply",
        description = "Commits the supply order to the supplier",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/{id}/order")
    public SupplyDto order(
        @PathVariable("id") UUID id
    ) {
        return supplyService.order(id);
    }


    /*
     * ============================================================
     * DELIVERY
     * ============================================================
     */

    @Operation(
        operationId = "deliverSupply",
        summary = "Record supply delivery",
        description = """
                      Records the goods delivered by the supplier.
                      The delivered items may differ from the original
                      order items.
                      """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Supply delivery recorded successfully",
            content = @Content(
                schema = @Schema(implementation = SupplyDto.class)
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
            description = "Supply not found",
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
    @PostMapping("/{id}/deliver")
    public SupplyDto deliver(
        @PathVariable("id") UUID id,
        @RequestBody
        @Valid
        DeliveryDto dto
    ) {
        return supplyService.deliver(id, dto);
    }


    @Operation(
        operationId = "submitSupplyDelivery",
        summary = "Submit supply delivery",
        description = "Submits the recorded delivery for approval",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/{id}/submit_delivery")
    public SupplyDto submitDelivery(
        @PathVariable("id") UUID id
    ) {
        return supplyService.submitDelivery(id);
    }


    @Operation(
        operationId = "approveSupplyDelivery",
        summary = "Approve supply delivery",
        description = "Approves the submitted delivery",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/{id}/approve_delivery")
    public SupplyDto approveDelivery(
        @PathVariable("id") UUID id
    ) {
        return supplyService.approveDelivery(id);
    }


    /*
     * ============================================================
     * RECEIVE
     * ============================================================
     */

    @Operation(
        operationId = "receiveSupply",
        summary = "Receive supply",
        description = """
                      Accepts the delivered goods into inventory.
                      This operation also creates the corresponding
                      stock movement.
                      """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Supply received successfully",
            content = @Content(
                schema = @Schema(implementation = SupplyDto.class)
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
            description = "Supply not found",
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
    public SupplyDto receive(
        @PathVariable("id") UUID id
    ) {
        return supplyService.receive(id);
    }


    /*
     * ============================================================
     * CANCEL
     * ============================================================
     */

    @Operation(
        operationId = "cancelSupply",
        summary = "Cancel supply",
        description = "Cancels a supply that has not yet been received",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/{id}/cancel")
    public SupplyDto cancel(
        @PathVariable("id") UUID id
    ) {
        return supplyService.cancel(id);
    }


    /*
     * ============================================================
     * DELETE
     * ============================================================
     */

    @Operation(
        operationId = "deleteSupply",
        summary = "Delete supply",
        description = "Deletes a supply while it is still eligible for deletion",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public SupplyDto delete(
        @PathVariable("id") UUID id
    ) {
        return supplyService.delete(id);
    }
}
