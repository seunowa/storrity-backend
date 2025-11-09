/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cart.controller;

import com.storrity.storrity.cart.dto.CartCreationDto;
import com.storrity.storrity.cart.dto.CartDto;
import com.storrity.storrity.cart.dto.CartItemInsertDto;
import com.storrity.storrity.cart.dto.CartUpdateDto;
import com.storrity.storrity.cart.dto.PricedCartDto;
import com.storrity.storrity.cart.entity.CartQueryParams;
import com.storrity.storrity.cart.service.CartService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/carts")
@Tag(name = "Carts", description = "Operations related to carts")
public class CartController {  
    
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }    
    
    @Operation(
            operationId = "createCart",
            description = "Creates a cart",
            summary = "Create a cart", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cart created successfully",
            content = @Content(schema = @Schema(implementation = PricedCartDto.class))
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
    public PricedCartDto create(@RequestBody @Valid CartCreationDto dto){
        return cartService.create(dto);
    }
    
    @Operation(
            operationId = "listCarts",
            description = "List a carts",
            summary = "List carts", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Carts retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = CartDto.class))
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
    public List<CartDto> list(@ModelAttribute @Valid  @ParameterObject CartQueryParams params){
        return cartService.list(params);
    }    
    
    @Operation(
            operationId = "CountCarts",
            description = "Count carts",
            summary = "Count carts", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Carts counted successfully",
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
    public CountDto count(@ModelAttribute @Valid  @ParameterObject CartQueryParams params){
        return cartService.count(params);
    }
    
    @Operation(
            operationId = "getCart",
            description = "Get a cart",
            summary = "Get a cart by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cart retrieved successfully",
            content = @Content(schema = @Schema(implementation = PricedCartDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cart not found",
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
    public PricedCartDto fetch(@PathVariable(name = "id") UUID id){
        return cartService.fetch(id);
    }
    
    @Operation(
            operationId = "updateCart",
            description = "Update a cart",
            summary = "Update cart",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cart updated successfully",
            content = @Content(schema = @Schema(implementation = PricedCartDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cart not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PatchMapping(path = "/{id}")
    public PricedCartDto update(@PathVariable(name = "id") UUID id, @RequestBody @Valid CartUpdateDto dto){
        return cartService.update(id, dto);
    }
    
    @Operation(
            operationId = "deleteCart",
            description = "Delete a cart by id",
            summary = "Delete a cart by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cart deleted successfully",
            content = @Content(schema = @Schema(implementation = PricedCartDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cart not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @DeleteMapping(path = "/{id}")
    public PricedCartDto delete(@PathVariable(name = "id") UUID id){
        return cartService.delete(id);
    }
    
    @Operation(
            operationId = "updateCartItem",
            description = "Update a cart item",
            summary = "Add or update cart item",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cart items updated successfully",
            content = @Content(schema = @Schema(implementation = PricedCartDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cart not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PutMapping(path = "/{id}/items")
    public PricedCartDto updateCartItems(@PathVariable(name = "id") UUID id, @RequestBody @Valid CartItemInsertDto dto){
        return cartService.updateCartItems(id, dto);
    }
    
    @Operation(
            operationId = "deleteCartItem",
            description = "Delete a cart item by id",
            summary = "Delete a cart item by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cart item deleted successfully",
            content = @Content(schema = @Schema(implementation = PricedCartDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cart item not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @DeleteMapping(path = "/items/{id}")
    public PricedCartDto deleteCartItem(@PathVariable(name = "id") UUID id){
        return cartService.deleteCartItem(id);
    }
}
