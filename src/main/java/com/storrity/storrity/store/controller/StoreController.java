/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.store.controller;

import com.storrity.storrity.store.dto.StoreCreationDto;
import com.storrity.storrity.store.dto.StoreUpdateDto;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.entity.StoreQueryParams;
import com.storrity.storrity.store.service.StoreService;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ApiError;
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
@RequestMapping("/api/v1/stores")
@Tag(name = "Stores", description = "Operations related to store management")
public class StoreController {
    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }    
    
    @Operation(
            operationId = "createStore",
            description = "Create a store",
            summary = "Create a store",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Store created successfully",
            content = @Content(schema = @Schema(implementation = Store.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PostMapping
    public Store create(@RequestBody @Valid StoreCreationDto dto){
        return storeService.create(dto);
    }
    
    @Operation(
            operationId = "listStores",
            description = "List stores",
            summary = "List stores",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stores retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = Store.class))
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping
    public List<Store> list(@ModelAttribute @Valid @ParameterObject StoreQueryParams params){
        return storeService.list(params);
    }
    
    @Operation(
            operationId = "countStores",
            description = "Count stores",
            summary = "Count stores",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stores counted successfully",
            content = @Content(schema = @Schema(implementation = CountDto.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping(path = "/count")
    public CountDto count(@ModelAttribute @Valid @ParameterObject StoreQueryParams params){
        return storeService.count(params);
    }
    
    @Operation(
            operationId = "getStore",
            description = "get a store by id",
            summary = "Get a stores by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Store retrieved successfully",
            content = @Content(schema = @Schema(implementation = Store.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Store not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping(path = "/{id}")
    public Store fetch(@PathVariable(name = "id") UUID id){
        return storeService.fetch(id);
    }
    
    @Operation(
            operationId = "updateStore",
            description = "Update a store",
            summary = "Update a store by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Store updated successfully",
            content = @Content(schema = @Schema(implementation = Store.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Store not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PatchMapping(path = "/{id}")
    public Store update(@PathVariable(name = "id") UUID id, @RequestBody @Valid StoreUpdateDto dto){
        return storeService.update(id, dto);
    }
    
    @Operation(
            operationId = "deleteStore",
            description = "delete a store",
            summary = "Delete a store by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Store deleted successfully",
            content = @Content(schema = @Schema(implementation = Store.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Store not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @DeleteMapping(path = "/{id}")
    public Store delete(@PathVariable(name = "id") UUID id){
        return storeService.delete(id);
    }
       
}
