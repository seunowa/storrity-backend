/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.controller;

import com.storrity.storrity.security.dto.UserProfileDto;
import com.storrity.storrity.security.dto.UserProfileUpdateDto;
import com.storrity.storrity.security.entity.UserProfileQueryParams;
import com.storrity.storrity.security.service.UserProfileService;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/user_profiles")
@Tag(name = "User Profiles", description = "Operations related to user profile management")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }
    
    @Operation(
            operationId = "listUserProfiles",
            description = "List a user profiles",
            summary = "List user profiles", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User profiles retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = UserProfileDto.class))
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
    public List<UserProfileDto> list(@ModelAttribute @Valid  @ParameterObject UserProfileQueryParams params){
        return userProfileService.list(params);
    }
    
    @Operation(
            operationId = "countUserProfiles",
            description = "Count user profiles",
            summary = "Count user profiles", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User profiles counted successfully",
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
    public CountDto count(@ModelAttribute @Valid  @ParameterObject UserProfileQueryParams params){
        return userProfileService.count(params);
    }
    
    @Operation(
            operationId = "getUserProfile",
            description = "Get a profile role by username",
            summary = "Get a user profile by username", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User profile retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserProfileDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User profile not found",
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
    @GetMapping(path = "/{username}")
    public UserProfileDto fetch(@PathVariable(name = "username") String username){
        return userProfileService.fetch(username);
    }
    
    @Operation(
            operationId = "updateUserProfile",
            description = "Update a profile role",
            summary = "Update a user profile by username", 
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User role updated successfully",
            content = @Content(schema = @Schema(implementation = UserProfileDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User role not found",
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
    @PatchMapping(path = "/{username}")
    public UserProfileDto update(@PathVariable(name = "username") String username, @RequestBody @Valid UserProfileUpdateDto dto){
        return userProfileService.update(username, dto);
    }
}
