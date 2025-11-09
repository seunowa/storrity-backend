/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.controller;

import com.storrity.storrity.security.entity.UserPermission;
import com.storrity.storrity.security.service.UserPermissionsService;
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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/user_permissions")
@Tag(name = "User permssions", description = "Operations related to user perrmission")
public class UserPermissionController {
    private final UserPermissionsService userPermissionsService;

    @Autowired
    public UserPermissionController(UserPermissionsService userPermissionsService) {
        this.userPermissionsService = userPermissionsService;
    }
    
    
    @Operation(
            operationId = "listUserPermissions",
            description = "List user permissions",
            summary = "List user permissions", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User permissions retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = UserPermission.class))
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
    public List<UserPermission> list(){
        return userPermissionsService.list();
    }
}
