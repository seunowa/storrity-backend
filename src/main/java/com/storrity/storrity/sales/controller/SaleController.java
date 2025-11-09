/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.controller;

import com.storrity.storrity.sales.dto.SaleDto;
import com.storrity.storrity.sales.dto.SalesCreationDto;
import com.storrity.storrity.sales.dto.SalesCreationResponse;
import com.storrity.storrity.sales.entity.SaleQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ApiError;
import com.storrity.storrity.util.exception.AuthenticationError;
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
import com.storrity.storrity.sales.service.SalesService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springdoc.core.annotations.ParameterObject;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/sales")
@Tag(name = "Sales", description = "Operations related to sales management")
public class SaleController {
    private final SalesService saleService;

    @Autowired
    public SaleController(SalesService saleService) {
        this.saleService = saleService;
    }
    
    @Operation(
            operationId = "createSale",
            description = "Create a sale",
            summary = "Create a sale", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sale created successfully",
            content = @Content(schema = @Schema(implementation = SalesCreationResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "403", 
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthenticationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PostMapping
    public SalesCreationResponse create(@RequestBody @Valid SalesCreationDto dto){
        return saleService.create(dto);
    }
    
    @Operation(
            operationId = "listSales",
            description = "List sales",
            summary = "List sales", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = SaleDto.class))
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "403", 
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthenticationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping
    public List<SaleDto> list(@ModelAttribute @Valid  @ParameterObject SaleQueryParams params){
        return saleService.list(params);
    }
    
    @Operation(
            operationId = "countSales",
            description = "Count sales",
            summary = "Count sales", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales counted successfully",
            content = @Content(schema = @Schema(implementation = CountDto.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "403", 
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthenticationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping(path = "/count")
    public CountDto count(@ModelAttribute @Valid  @ParameterObject SaleQueryParams params){
        return saleService.count(params);
    }
    
    @Operation(
            operationId = "getSale",
            description = "Get a sale",
            summary = "Get a sale by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sale retrieved successfully",
            content = @Content(schema = @Schema(implementation = SaleDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Sale not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthenticationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping(path = "/{id}")
    public SaleDto fetch(@PathVariable(name = "id") UUID id){
        return saleService.fetch(id);
    }
}
