/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.controller;

import com.storrity.storrity.sales.dto.SalesReturnCreationDto;
import com.storrity.storrity.sales.dto.SalesReturnDto;
import com.storrity.storrity.sales.entity.SalesReturnQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ApiError;
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
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.storrity.storrity.sales.service.SalesReturnService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springdoc.core.annotations.ParameterObject;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/sales_return")
@Tag(name = "Sales Return", description = "Operations related to sales return management")
public class SalesReturnController {
    private final SalesReturnService salesReturnService;

    @Autowired
    public SalesReturnController(SalesReturnService saleReturnService) {
        this.salesReturnService = saleReturnService;
    }
    
    @Operation(
            operationId = "createSalesReturn",
            description = "Create a sales return",
            summary = "Create a sales return", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales return created successfully",
            content = @Content(schema = @Schema(implementation = SalesReturnDto.class))
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
    public SalesReturnDto create(@RequestBody @Valid SalesReturnCreationDto dto){
        return salesReturnService.create(dto);
    }
    
    @Operation(
            operationId = "listSalesReturns",
            description = "List sales returns",
            summary = "List sales returns", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales returns retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = SalesReturnDto.class))
            )
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
    @GetMapping
    public List<SalesReturnDto> list(@ModelAttribute @Valid  @ParameterObject SalesReturnQueryParams params){
        return salesReturnService.list(params);
    }
    
    @Operation(
            operationId = "countSalesReturns",
            description = "Count sales returns",
            summary = "Count sales returns", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales returns counted successfully",
            content = @Content(schema = @Schema(implementation = CountDto.class))
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
    @GetMapping(path = "/count")
    public CountDto count(@ModelAttribute @Valid  @ParameterObject SalesReturnQueryParams params){
        return salesReturnService.count(params);
    }
    
    @Operation(
            operationId = "getSalesReturn",
            description = "Get a sales return",
            summary = "Get a sales return by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales return retrieved successfully",
            content = @Content(schema = @Schema(implementation = SalesReturnDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Sales return not found",
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
    @GetMapping(path = "/{id}")
    public SalesReturnDto fetch(@PathVariable(name = "id") UUID id){
        return salesReturnService.fetch(id);
    }
}
