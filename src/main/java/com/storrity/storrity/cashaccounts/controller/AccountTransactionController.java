/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.controller;

import com.storrity.storrity.cashaccounts.dto.AccountTransactionInstruction;
import com.storrity.storrity.cashaccounts.entity.AccountTransaction;
import com.storrity.storrity.cashaccounts.entity.AccountTransactionQueryParams;
import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.service.AccountTransactionService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/api/v1/account_transactions")
@Tag(name = "Cash Account Transactions", description = "Operations related to cash account transactions")
public class AccountTransactionController {
    private final AccountTransactionService accountTransactionService;

    public AccountTransactionController(AccountTransactionService accountTransactionService) {
        this.accountTransactionService = accountTransactionService;
    }   
    
    @Operation(
            operationId = "createAcountTransaction",
            description = "Create an account transaction",
            summary = "Create an account transaction", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account transaction created successfully",
            content = @Content(schema = @Schema(implementation = AccountTransaction.class))
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
    public AccountTransaction create(@RequestBody @Valid AccountTransactionInstruction dto){
        return accountTransactionService.create(dto);
    }
    
    @Operation(
            operationId = "listAcountTransactions",
            description = "List account transactions",
            summary = "List cash account transactions",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account transactions retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = AccountTransaction.class))
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
    public List<AccountTransaction> list(@ModelAttribute @Valid  @ParameterObject AccountTransactionQueryParams params){
        return accountTransactionService.list(params);
    }
    
    @Operation(
            operationId = "countAcountTransactions",
            description = "Count account transactions",
            summary = "Count account transactions",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account transaction counted successfully",
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
    public CountDto count(@ModelAttribute @Valid  @ParameterObject AccountTransactionQueryParams params){
        return accountTransactionService.count(params);
    }    
    
    @Operation(
            operationId = "getAccountTransaction",
            description = "Get an account transaction by id",
            summary = "Get an account transaction by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account transaction retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account transaction not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping(path = "/{id}")
    public AccountTransaction fetch(@PathVariable(name = "id") UUID id){
        return accountTransactionService.fetch(id);
    }
}
