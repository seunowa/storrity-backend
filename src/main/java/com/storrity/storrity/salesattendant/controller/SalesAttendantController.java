/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.salesattendant.controller;

import com.storrity.storrity.salesattendant.dto.SalesAttendantCreationDto;
import com.storrity.storrity.salesattendant.dto.SalesAttendantUpdateDto;
import com.storrity.storrity.salesattendant.entity.SalesAttendant;
import com.storrity.storrity.salesattendant.entity.SalesAttendantQueryParams;
import com.storrity.storrity.salesattendant.service.SalesAttendantService;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ApiError;
import com.storrity.storrity.util.exception.AuthenticationError;
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
@RequestMapping("/api/v1/sales_attendance")
@Tag(name = "Sales Attendant", description = "Operations related to sales attendance management")
public class SalesAttendantController {
    private final SalesAttendantService salesAttendantService;

    @Autowired
    public SalesAttendantController(SalesAttendantService salesAttendantService) {
        this.salesAttendantService = salesAttendantService;
    }
    
    @Operation(
            operationId = "createSalesAttendant",
            description = "Create a sales attendant",
            summary = "Create a sales attendant", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales attendant created successfully",
            content = @Content(schema = @Schema(implementation = SalesAttendant.class))
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
    public SalesAttendant create(@RequestBody @Valid SalesAttendantCreationDto dto){
        return salesAttendantService.create(dto);
    }
    
    @Operation(
            operationId = "listSalesAttendants",
            description = "List sales attendants",
            summary = "List Sales attendants", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales attendants retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = SalesAttendant.class))
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
    public List<SalesAttendant> list(@ModelAttribute @Valid  @ParameterObject SalesAttendantQueryParams params){
        return salesAttendantService.list(params);
    }
    
    @Operation(
            operationId = "countSalesAttendants",
            description = "Count sales attendants",
            summary = "Count Sales attendants", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales attendants counted successfully",
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
    public CountDto count(@ModelAttribute @Valid  @ParameterObject SalesAttendantQueryParams params){
        return salesAttendantService.count(params);
    }
    
    @Operation(
            operationId = "getSalesAttendant",
            description = "Get a sales attendant",
            summary = "Get a Sales attendant by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales attendant retrieved successfully",
            content = @Content(schema = @Schema(implementation = SalesAttendant.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Sales attendant not found",
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
    public SalesAttendant fetch(@PathVariable(name = "id") UUID id){
        return salesAttendantService.fetch(id);
    }
    
    @Operation(
            operationId = "updateSalesAttendant",
            description = "Update a sales attendant",
            summary = "Update a Sales attendant by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales attendant updated successfully",
            content = @Content(schema = @Schema(implementation = SalesAttendant.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Sales attendant not found",
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
    @PatchMapping(path = "/{id}")
    public SalesAttendant update(@PathVariable(name = "id") UUID id, @RequestBody @Valid SalesAttendantUpdateDto dto){
        return salesAttendantService.update(id, dto);
    }
    
    @Operation(
            operationId = "deleteSalesAttendant",
            description = "Delete a sales attendant",
            summary = "Delete a Sales attendant by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sales attendant deleted successfully",
            content = @Content(schema = @Schema(implementation = SalesAttendant.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Sales attendant not found",
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
    @DeleteMapping(path = "/{id}")
    public SalesAttendant delete(@PathVariable(name = "id") UUID id){
        return salesAttendantService.delete(id);
    }
}
