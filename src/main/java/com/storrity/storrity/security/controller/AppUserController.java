/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.controller;

import com.storrity.storrity.security.dto.*;
import com.storrity.storrity.security.entity.AppUserQueryParams;
import com.storrity.storrity.security.service.AppUserService;
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
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Operations related to user management")
public class AppUserController {
    private final AppUserService userService;

    @Autowired
    public AppUserController(AppUserService userService) {
        this.userService = userService;
    }

    @Operation(
            operationId = "createUser",
            description = "Create a user",
            summary = "Create a new user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserDetailDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403",
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthenticationError.class))),        
        @ApiResponse(
            responseCode = "404",
            description = "Role not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @PostMapping
//    @PreAuthorize("hasAuthority('user:create')")
    public UserDetailDto create(@RequestBody @Valid UserCreationDto dto) {
        return userService.create(dto);
    }

    @Operation(
            operationId = "changePassword",
            description = "change password",
            summary = "Change user password",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Password changed successfully",
            content = @Content(schema = @Schema(implementation = UserDetailDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403",
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthenticationError.class))),        
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @PatchMapping("/password")
//    @PreAuthorize("hasAuthority('user:password:change')")
    public UserDetailDto changePassword(@RequestBody @Valid PasswordChangeDto dto) {
        return userService.changePassword(dto);
    }

    @Operation(
            operationId = "resetPassword",
            description = "reset password",
            summary = "Reset user password",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Password reset successfully",
            content = @Content(schema = @Schema(implementation = UserDetailDto.class))
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
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @PatchMapping("/password/reset")
//    @PreAuthorize("hasAuthority('user:password:reset')")
    public UserDetailDto resetPassword(@RequestBody @Valid PasswordRestDto dto) {
        return userService.resetPassword(dto);
    }

    @Operation(
            operationId = "changeUserRole",
            description = "change user role",
            summary = "Change user role",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Role changed successfully",
            content = @Content(schema = @Schema(implementation = UserDetailDto.class))
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
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @PatchMapping("/role")
//    @PreAuthorize("hasAuthority('user:role:change')")
    public UserDetailDto changeRole(@RequestBody @Valid RoleChangeDto dto) {
        return userService.changeRole(dto);
    }
    
    @Operation(
            operationId = "listUsers",
            description = "list users",
            summary = "List users",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Users retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = UserDetailDto.class))
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
            content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping
//    @PreAuthorize("hasAuthority('user:read')")
    public List<UserDetailDto> list(@ModelAttribute @Valid  @ParameterObject AppUserQueryParams params) {
        return userService.list(params);
    }

    @Operation(
            operationId = "countUsers",
            description = "count user",
            summary = "Count users",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Users counted successfully",
            content = @Content(schema = @Schema(implementation = CountDto.class))),
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
            content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping("/count")
//    @PreAuthorize("hasAuthority('user:read')")
    public CountDto count(@ModelAttribute @Valid  @ParameterObject AppUserQueryParams params) {
        return userService.count(params);
    }    

    @Operation(
            operationId = "fetchUser",
            description = "fetch user",
            summary = "Fetch user by username",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User retrived successfully",
            content = @Content(schema = @Schema(implementation = UserDetailDto.class))
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
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @GetMapping("/{username}")
//    @PreAuthorize("hasAuthority('user:read')")
    public UserDetailDto fetch(@PathVariable(name = "username") String username) {
        return userService.fetch(username);
    }

    @Operation(
            operationId = "deleteUser",
            description = "delete user",
            summary = "Delete user by username",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User deleted successfully",
            content = @Content(schema = @Schema(implementation = UserDetailDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "403",
            description = "Authentication Error",
            content = @Content(schema = @Schema(implementation = AuthenticationError.class))),        
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class)))
    })
    @DeleteMapping("/{username}")
//    @PreAuthorize("hasAuthority('user:delete')")
    public UserDetailDto delete(@PathVariable(name = "username") String username) {
        return userService.delete(username);
    }
}
