/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.controller;

import com.storrity.storrity.cashaccounts.entity.StatementItem;
import com.storrity.storrity.cashaccounts.entity.StatementItemQueryParams;
import com.storrity.storrity.cashaccounts.service.StatementItemService;
import com.storrity.storrity.store.entity.Store;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/account_statements")
@Tag(name = "Account Statements", description = "Operations related to account statements")
public class StatementItemController {
    private final StatementItemService statementItemService;

    @Autowired
    public StatementItemController(StatementItemService statementItemService) {
        this.statementItemService = statementItemService;
    }    
    
    @Operation(
            operationId = "listAccountStatements",
            description = "List account statements",
            summary = "List account statement items",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account statement items retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = StatementItem.class))
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
    public List<StatementItem> list(@ModelAttribute @Valid  @ParameterObject StatementItemQueryParams params){
        return statementItemService.list(params);
    }
    
    @Operation(
            operationId = "countAccountStatements",
            description = "Count account statements",
            summary = "Count account statement items",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account statement items counted successfully",
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
    public CountDto count(@ModelAttribute @Valid  @ParameterObject StatementItemQueryParams params){
        return statementItemService.count(params);
    }
    
    @Operation(
            operationId = "getAccountStatement",
            description = "Get a account statement by id",
            summary = "Get an account statement item by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account statement item retrieved successfully",
            content = @Content(schema = @Schema(implementation = StatementItem.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account statement item not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping(path = "/{id}")
    public StatementItem fetch(@PathVariable(name = "id") UUID id){
        return statementItemService.fetch(id);
    }
}
