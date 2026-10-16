/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.controller;

import com.storrity.storrity.cashaccounts.dto.AccountValidationDto;
import com.storrity.storrity.cashaccounts.dto.CashAccountCreationDto;
import com.storrity.storrity.cashaccounts.dto.CashAccountUpdateDto;
import com.storrity.storrity.cashaccounts.dto.ChangeParentAccountInstruction;
import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.entity.CashAccountQueryParams;
import com.storrity.storrity.cashaccounts.service.CashAccountService;
import com.storrity.storrity.cashaccounts.service.ParentAccountService;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ApiError;
import com.storrity.storrity.util.exception.ServerError;
import com.storrity.storrity.util.exception.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.validation.Valid;
import java.util.List;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/cashaccounts")
@Tag(name = "Cash Accounts", description = "Operations related to cash accounts")
public class CashAccountController {
    
    private final CashAccountService cashAccountService;
    private final ParentAccountService parentAccountService;

    @Autowired
    public CashAccountController(CashAccountService cashAccountService, ParentAccountService parentAccountService) {
        this.cashAccountService = cashAccountService;
        this.parentAccountService = parentAccountService;
    }    
    
    @Operation(
            operationId = "createCashAccount",
            description = "Create a cash account "
                + "requires permission: CASH_ACCOUNT_CREATE",
            summary = "Create a cash account", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account created successfully",
            content = @Content(schema = @Schema(implementation = CashAccount.class))
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
    @PreAuthorize("hasAuthority('CASH_ACCOUNT_CREATE')")
    public CashAccount create(@RequestBody @Valid CashAccountCreationDto dto){
        return cashAccountService.create(dto);
    }
    
    @Operation(
            operationId = "listCashAccounts",
            description = "List cash accounts "
                + "requires permission: CASH_ACCOUNT_READ",
            summary = "List cash accounts",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Accounts retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = CashAccount.class))
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
    @PreAuthorize("hasAuthority('CASH_ACCOUNT_READ')")
    public List<CashAccount> list(@ModelAttribute @Valid  @ParameterObject CashAccountQueryParams params){
        return cashAccountService.list(params);
    }
    
    @Operation(
            operationId = "countCashAccounts",
            description = "Count cash accounts "
                + "requires permission: CASH_ACCOUNT_READ",
            summary = "Count cash accounts",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Accounts counted successfully",
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
    @PreAuthorize("hasAuthority('CASH_ACCOUNT_READ')")
    public CountDto count(@ModelAttribute @Valid  @ParameterObject CashAccountQueryParams params){
        return cashAccountService.count(params);
    }
    
    @Operation(
            operationId = "getCashAccount",
            description = "Get a cash account by id "
                + "requires permission: CASH_ACCOUNT_READ",
            summary = "Get a cash account by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account retrieved successfully",
            content = @Content(schema = @Schema(implementation = CashAccount.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('CASH_ACCOUNT_READ')")
    public CashAccount fetch(@PathVariable(name = "id") String id){
        return cashAccountService.fetch(id);
    }
    
    @Operation(
            operationId = "validateAccount",
            description = "Validate a cash account by id "
                + "requires permission: CASH_ACCOUNT_READ",
            summary = "Validate a cash account by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account validation retrieved successfully",
            content = @Content(schema = @Schema(implementation = AccountValidationDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping(path = "/{id}/validation")
    @PreAuthorize("hasAuthority('CASH_ACCOUNT_READ')")
    public AccountValidationDto validate(@PathVariable(name = "id") String id){
        return cashAccountService.validateAccount(id);
    }
    
    @Operation(
            operationId = "updateCashAccount",
            description = "Update a cash account "
                + "requires permission: CASH_ACCOUNT_UPDATE",
            summary = "Update a cash account by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account updated successfully",
            content = @Content(schema = @Schema(implementation = CashAccount.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('CASH_ACCOUNT_UPDATE')")
    public CashAccount update(@PathVariable(name = "id") String id, @RequestBody @Valid CashAccountUpdateDto dto){
        return cashAccountService.update(id, dto);
    }
    
    @Operation(
            operationId = "deleteCashAccount",
            description = "Delete a cash account "
                + "requires permission: CASH_ACCOUNT_DELETE",
            summary = "Delete a cash account by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account deleted successfully",
            content = @Content(schema = @Schema(implementation = CashAccount.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('CASH_ACCOUNT_DELETE')")
    public CashAccount delete(@PathVariable(name = "id") String id){
        return cashAccountService.delete(id);
    }
    
    @Operation(
            operationId = "updateCashAccountParent",
            description = "Update a cash account's parent by id "
                + "requires permission: CASH_ACCOUNT_PARENT_UPDATE",
            summary = "Update a cash account's parent by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account updated successfully",
            content = @Content(schema = @Schema(implementation = CashAccount.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PatchMapping(path = "/{id}/parent")
    @PreAuthorize("hasAuthority('CASH_ACCOUNT_PARENT_UPDATE')")
    public CashAccount update(@PathVariable(name = "id") String id, @RequestBody @Valid ChangeParentAccountInstruction dto){
        dto.setAccountId(id);
        return parentAccountService.changeParentAccount(dto);
    }  
}
