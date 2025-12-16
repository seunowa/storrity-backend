/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.licernse.controller;

import com.storrity.storrity.license.dto.ClientSystemCreationDto;
import com.storrity.storrity.license.dto.ClientSystemUpdateDto;
import com.storrity.storrity.license.entity.ClientSystem;
import com.storrity.storrity.license.repository.ClientSystemQueryParams;
import com.storrity.storrity.license.service.ClientSystemService;
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
@RequestMapping("/api/v1/client_systems")
@Tag(name = "Client System", description = "Operations related to client system management")
public class ClientSystemController {
    private final ClientSystemService clientSystemService;

    @Autowired
    public ClientSystemController(ClientSystemService clientSystemService) {
        this.clientSystemService = clientSystemService;
    }    
    
    @Operation(
            operationId = "createClientSystem",
            description = "Create a client system",
            summary = "Create a client system",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Client system created successfully",
            content = @Content(schema = @Schema(implementation = ClientSystem.class))
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
    public ClientSystem create(@RequestBody @Valid ClientSystemCreationDto dto){
        return clientSystemService.create(dto);
    }
    
    @Operation(
            operationId = "listClientSystem",
            description = "List client systems",
            summary = "List client systems",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Client system retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = ClientSystem.class))
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
    public List<ClientSystem> list(@ModelAttribute @Valid @ParameterObject ClientSystemQueryParams params){
        return clientSystemService.list(params);
    }
    
    @Operation(
            operationId = "countClientSystem",
            description = "Count client system",
            summary = "Count Client systems",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Client system counted successfully",
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
    public CountDto count(@ModelAttribute @Valid @ParameterObject ClientSystemQueryParams params){
        return clientSystemService.count(params);
    }
    
    @Operation(
            operationId = "getClientSystem",
            description = "get a client system by id",
            summary = "Get a client system by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Client system retrieved successfully",
            content = @Content(schema = @Schema(implementation = ClientSystem.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Client system not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping(path = "/{id}")
    public ClientSystem fetch(@PathVariable(name = "id") UUID id){
        return clientSystemService.fetch(id);
    }
    
    @Operation(
            operationId = "updateClientSystem",
            description = "Update a client system",
            summary = "Update a client system by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Client system updated successfully",
            content = @Content(schema = @Schema(implementation = ClientSystem.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Client system not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PatchMapping(path = "/{id}")
    public ClientSystem update(@PathVariable(name = "id") UUID id, @RequestBody @Valid ClientSystemUpdateDto dto){
        return clientSystemService.update(id, dto);
    }
    
    @Operation(
            operationId = "deleteClientSystem",
            description = "delete a client system",
            summary = "Delete a client system by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Client system deleted successfully",
            content = @Content(schema = @Schema(implementation = ClientSystem.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Client system not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @DeleteMapping(path = "/{id}")
    public ClientSystem delete(@PathVariable(name = "id") UUID id){
        return clientSystemService.delete(id);
    }
}
