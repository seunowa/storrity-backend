/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.product.controller;

import com.storrity.storrity.product.dto.ProductCreationDto;
import com.storrity.storrity.product.dto.ProductDto;
import com.storrity.storrity.product.dto.ProductUpdateDto;
import com.storrity.storrity.product.entity.ProductQueryParams;
import com.storrity.storrity.product.service.ProductService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Operations related to product management")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @Operation(
            operationId = "createProduct",
            description = "Creates a product",
            summary = "Create a product", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product created successfully",
            content = @Content(schema = @Schema(implementation = ProductDto.class))
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
    public ProductDto create(@RequestBody @Valid ProductCreationDto dto){
        return productService.create(dto);
    }
    
    @Operation(
            operationId = "listProducts",
            description = "List products",
            summary = "List products", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Products retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = ProductDto.class))
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
    public List<ProductDto> list(@ModelAttribute @Valid  @ParameterObject ProductQueryParams params){
        return productService.list(params);
    }
    
    @Operation(
            operationId = "countProducts",
            description = "count products",
            summary = "Count products", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Products counted successfully",
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
    public CountDto count(@ModelAttribute @Valid  @ParameterObject ProductQueryParams params){
        return productService.count(params);
    }     
    
    @Operation(
            operationId = "listProductNames",
            description = "List product names",
            summary = "List product names", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product names retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = String.class))
            )
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
    @GetMapping(path = "/names")
    public List<String> listProductNames(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) UUID storeId){
        return productService.listProductNames(query, limit, storeId);
    }      
    
    @Operation(
            operationId = "listCategories",
            description = "List product categories",
            summary = "List product categories", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product categories retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = String.class))
            )
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
    @GetMapping(path = "/categories")
    public List<String> listCategories(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) UUID storeId){
        return productService.listCategories(query, limit, storeId);
    }    
    
    @Operation(
            operationId = "listSubcategories",
            description = "List product subcategories",
            summary = "List product subcategories", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product subcategories retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = String.class))
            )
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
    @GetMapping(path = "/subcategories")
    public List<String> listSubcategories(    
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) UUID storeId){
        return productService.listSubcategories(query, limit, storeId);
    }    
    
    @Operation(
            operationId = "listBrands",
            description = "List product brands",
            summary = "List product brands", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product brands retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = String.class))
            )
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
    @GetMapping(path = "/brands")
    public List<String> findBrands(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) UUID storeId){
        return productService.listBrands(query, limit, storeId);
    }
    
    
    @Operation(
            operationId = "listStockKeepingUnits",
            description = "List product stock keeping units",
            summary = "List product stock keeping units", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product stock keeping units retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = String.class))
            )
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
    @GetMapping(path = "/sku")
    public List<String> listStockKeepingUnits(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) UUID storeId){
        return productService.listStockKeepingUnits(query, limit, storeId);
    }
    
    
    @Operation(
            operationId = "listPackages",
            description = "List packages",
            summary = "List packages", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Packages retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = String.class))
            )
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
    @GetMapping(path = "/packages")
    public List<String> listPackages(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) UUID storeId){
        return productService.listPackages(query, limit, storeId);
    }
    
    @Operation(
            operationId = "getProduct",
            description = "Get a product by id",
            summary = "Get a product by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product retrieved successfully",
            content = @Content(schema = @Schema(implementation = ProductDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
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
    public ProductDto fetch(@PathVariable(name = "id") UUID id){
        return productService.fetch(id);
    }
    
    @Operation(
            operationId = "updateProduct",
            description = "Update a product",
            summary = "Update a product by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product updated successfully",
            content = @Content(schema = @Schema(implementation = ProductDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
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
    @PatchMapping(path = "/{id}")
    public ProductDto update(@PathVariable(name = "id") UUID id, @RequestBody @Valid ProductUpdateDto dto){
        return productService.update(id, dto);
    }
    
    @Operation(
            operationId = "deleteProduct",
            description = "Delete a product",
            summary = "Delete a product by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product deleted successfully",
            content = @Content(schema = @Schema(implementation = ProductDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
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
    @DeleteMapping(path = "/{id}")
    public ProductDto delete(@PathVariable(name = "id") UUID id){
        return productService.delete(id);
    }    
    
}
