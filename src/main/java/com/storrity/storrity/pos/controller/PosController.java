/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.pos.controller;

import com.storrity.storrity.pos.dto.PosCreationDto;
import com.storrity.storrity.pos.dto.PosUpdateDto;
import com.storrity.storrity.pos.entity.Pos;
import com.storrity.storrity.pos.entity.PosQueryParam;
import com.storrity.storrity.pos.service.PosService;
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
@RequestMapping("/api/v1/pos")
@Tag(name = "POS", description = "Operations related to POS management")
public class PosController {
    
    private final PosService posService;

    @Autowired
    public PosController(PosService posService) {
        this.posService = posService;
    }
    
    @Operation(
            operationId = "createPos",
            description = "Creates a Pos",
            summary = "Create a POS", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "POS created successfully",
            content = @Content(schema = @Schema(implementation = Pos.class))
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
    public Pos create(@RequestBody @Valid PosCreationDto dto){
        return posService.create(dto);
    }
    
    @Operation(
            operationId = "listPos",
            description = "List Pos",
            summary = "List POS", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "POS retrieved successfully"
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
    public List<Pos> list(@ModelAttribute @Valid  @ParameterObject PosQueryParam params){
        return posService.list(params);
    }
    
    @Operation(
            operationId = "countPos",
            description = "Count Pos",
            summary = "Count a POS", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "POS counted successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = Pos.class))
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
    @GetMapping(path = "/count")
    public CountDto count(@ModelAttribute @Valid  @ParameterObject PosQueryParam params){
        return posService.count(params);
    }
    
    @Operation(
            operationId = "getPos",
            description = "Get a Pos by Id",
            summary = "Get a usre role by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "POD retrieved successfully",
            content = @Content(schema = @Schema(implementation = CountDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "POS not found",
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
    public Pos fetch(@PathVariable(name = "id") UUID id){
        return posService.fetch(id);
    }
    
    @Operation(
            operationId = "updatePos",
            description = "Update a Pos",
            summary = "Update a POS by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "POS updated successfully",
            content = @Content(schema = @Schema(implementation = Pos.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "POS not found",
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
    public Pos update(@PathVariable(name = "id") UUID id, @RequestBody @Valid PosUpdateDto dto){
        return posService.update(id, dto);
    }
    
    @Operation(
            operationId = "deletePos",
            description = "Delete a Pos",
            summary = "Delete a user role by ID", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "POS deleted successfully",
            content = @Content(schema = @Schema(implementation = Pos.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "POS not found",
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
    public Pos delete(@PathVariable(name = "id") UUID id){
        return posService.delete(id);
    }
}
