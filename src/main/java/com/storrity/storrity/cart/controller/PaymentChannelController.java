package com.storrity.storrity.cart.controller;

import com.storrity.storrity.cart.dto.PaymentChannelCreateRequest;
import com.storrity.storrity.cart.dto.PaymentChannelUpdateRequest;
import com.storrity.storrity.cart.entity.PaymentChannel;
import com.storrity.storrity.cart.entity.PaymentChannelQueryParams;
import com.storrity.storrity.cart.service.PaymentChannelService;
import com.storrity.storrity.util.dto.CountDto;
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
import java.util.List;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */

@CrossOrigin
@RestController
@RequestMapping("/api/v1/payment_channels")
@Tag(name = "Payment Channels", description = "Manage store payment channels")
public class PaymentChannelController {
    private final PaymentChannelService service;

    public PaymentChannelController(PaymentChannelService service) {
        this.service = service;
    }

    @Operation(operationId = "createPaymentChannel",
            summary = "Create a payment channel",
            description = "Creates a payment channel for a store. Requires permission: PAYMENT_CHANNEL_CREATE",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Payment channel created",
                content = @Content(schema = @Schema(implementation = PaymentChannel.class))),
        @ApiResponse(responseCode = "400", description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403", description = "Authorization Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('PAYMENT_CHANNEL_CREATE')")
    public PaymentChannel create(@Valid @RequestBody PaymentChannelCreateRequest request) {
        return service.create(request);
    }

    @Operation(operationId = "listPaymentChannels",
            summary = "List payment channels",
            description = "Lists payment channels using optional filters. Requires permission: PAYMENT_CHANNEL_READ",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment channels retrieved",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = PaymentChannel.class)))),
        @ApiResponse(responseCode = "400", description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403", description = "Authorization Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping
    @PreAuthorize("hasAuthority('PAYMENT_CHANNEL_READ')")
    public List<PaymentChannel> list(@Valid @ModelAttribute @ParameterObject PaymentChannelQueryParams params) {
        return service.list(params);
    }

    @Operation(operationId = "countPaymentChannels",
            summary = "Count payment channels",
            description = "Counts payment channels matching the supplied filters. Requires permission: PAYMENT_CHANNEL_READ",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment channels counted",
                content = @Content(schema = @Schema(implementation = CountDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403", description = "Authorization Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping("/count")
    @PreAuthorize("hasAuthority('PAYMENT_CHANNEL_READ')")
    public CountDto count(@Valid @ModelAttribute @ParameterObject PaymentChannelQueryParams params) {
        return service.count(params);
    }

    @Operation(operationId = "getPaymentChannel",
            summary = "Get a payment channel",
            description = "Retrieves a payment channel by ID, including inactive channels. Requires permission: PAYMENT_CHANNEL_READ",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment channel retrieved",
                content = @Content(schema = @Schema(implementation = PaymentChannel.class))),
        @ApiResponse(responseCode = "404", description = "Payment channel not found",
                content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Authorization Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_CHANNEL_READ')")
    public PaymentChannel get(@Parameter(description = "Payment channel UUID") @PathVariable UUID id) {
        return service.fetch(id);
    }

    @Operation(operationId = "updatePaymentChannel",
            summary = "Update a payment channel",
            description = "Updates the channel name. Store, code and payment method are immutable. Requires permission: PAYMENT_CHANNEL_UPDATE",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment channel updated",
                content = @Content(schema = @Schema(implementation = PaymentChannel.class))),
        @ApiResponse(responseCode = "400", description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "404", description = "Payment channel not found",
                content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Authorization Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_CHANNEL_UPDATE')")
    public PaymentChannel update(@PathVariable UUID id, @Valid @RequestBody PaymentChannelUpdateRequest request) {
        return service.update(id, request);
    }

    @Operation(operationId = "activatePaymentChannel",
            summary = "Activate a payment channel",
            description = "Makes a payment channel available for new transactions. Requires permission: PAYMENT_CHANNEL_UPDATE",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment channel activated",
                content = @Content(schema = @Schema(implementation = PaymentChannel.class))),
        @ApiResponse(responseCode = "404", description = "Payment channel not found",
                content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Authorization Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('PAYMENT_CHANNEL_UPDATE')")
    public PaymentChannel activate(@PathVariable UUID id) {
        return service.activate(id);
    }

    @Operation(operationId = "deactivatePaymentChannel",
            summary = "Deactivate a payment channel",
            description = "Prevents a payment channel from being used for new transactions without deleting historical records. Requires permission: PAYMENT_CHANNEL_UPDATE",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment channel deactivated",
                content = @Content(schema = @Schema(implementation = PaymentChannel.class))),
        @ApiResponse(responseCode = "404", description = "Payment channel not found",
                content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Authorization Error",
                content = @Content(schema = @Schema(implementation = AuthorizationError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('PAYMENT_CHANNEL_UPDATE')")
    public PaymentChannel deactivate(@PathVariable UUID id) {
        return service.deactivate(id);
    }
}