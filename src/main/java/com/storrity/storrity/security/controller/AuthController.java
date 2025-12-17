/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.controller;

import com.storrity.storrity.license.dto.ClientSystemCreationDto;
import com.storrity.storrity.license.entity.ClientSystemStatus;
import com.storrity.storrity.license.service.ClientSystemService;
import com.storrity.storrity.license.service.LicenseService;
import com.storrity.storrity.security.dto.LoginRequestDto;
import com.storrity.storrity.security.dto.LoginSuccessDto;
import com.storrity.storrity.security.dto.RootUserInitSuccessDto;
import com.storrity.storrity.security.dto.UserCreationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.storrity.storrity.security.entity.AppUser;
import com.storrity.storrity.security.entity.UserPermission;
import com.storrity.storrity.security.entity.UserRole;
import com.storrity.storrity.security.repository.UserRepository;
import com.storrity.storrity.security.repository.UserRoleRepository;
import com.storrity.storrity.security.service.AppUserService;
import com.storrity.storrity.security.service.JwtUtil;
import com.storrity.storrity.util.exception.BadRequestAppException;
import com.storrity.storrity.util.exception.ServerError;
import com.storrity.storrity.util.exception.ValidationError;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authorization", description = "Operations related to authorization")
public class AuthController {

    private final UserRepository userRepo;
    private final UserRoleRepository roleRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final AppUserService appUserService;
    private final ClientSystemService clientSystemService;
    private final LicenseService licenseService;

    public AuthController(UserRepository userRepo, UserRoleRepository roleRepo
            , JwtUtil jwtUtil, PasswordEncoder encoder, AppUserService appUserService
            , ClientSystemService clientSystemService, LicenseService licenseService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
        this.appUserService = appUserService;
        this.clientSystemService = clientSystemService;
        this.licenseService = licenseService;
    }
    
    @Operation(
        operationId = "login",
        description = "login",
        summary = "Login and obtain JWT token"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Login successful"
//            content = @Content(schema = @Schema(implementation = Store.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid username or password"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        
//        @Todo check if clientId is registered (is licensed)
        licenseService.isClientSystemLicensed(request.getClientId());
        
        AppUser user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        var authorities = user.getRole().getPermissions().stream()
                .map(p -> "PERM_" + p.name())
                .toList();

        String token = jwtUtil.generateToken(user.getUsername(), request.getClientId(), authorities);
        return ResponseEntity.ok(Map.of("token", token));
//        return LoginSuccessDto.builder().build();
    }
    
    @Operation(
        operationId = "init",
        description = "initialize root user",
        summary = "Initialize root user"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Root user initialization successful",
            content = @Content(schema = @Schema(implementation = RootUserInitSuccessDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PostMapping("/init")
    public RootUserInitSuccessDto init(@Valid @RequestBody LoginRequestDto request) {
        
//        @Todo check if the system has at least one and user is activated... consider other steps to take
        long noOfUsers = userRepo.count();
        if(noOfUsers > 0){
            throw new BadRequestAppException("You are not allowed to reinitialize root user");
        }
        
        // Create admin role with all permissions
        Set<UserPermission> allPerms = EnumSet.allOf(UserPermission.class);
        UserRole adminRole = new UserRole();
        adminRole.setId("ADMIN");
        adminRole.setPermissions(allPerms);
        roleRepo.save(adminRole);
        
        // Create user
        UserCreationDto dto = new UserCreationDto();
        dto.setUsername(request.getUsername());
        dto.setPassword(request.getPassword());
        dto.setRole(adminRole.getId());
        appUserService.create(dto);

        System.out.println("✅ Added admin user with username");
        
        //Create Create client system
        ClientSystemCreationDto cscDto = ClientSystemCreationDto
                .builder()
                .clientId(request.getClientId())
                .name("Client1")
                .status(ClientSystemStatus.ACTIVE)
                .build();
                clientSystemService.create(cscDto);
                
        System.out.println("✅ Added client system: Client1");
        
        return RootUserInitSuccessDto.builder().message("Root user added succefully").build();
    }

//    @PostConstruct
    public void seedAdmin() {
        if (userRepo.findByUsername("admin").isPresent()) return;

        // Create admin role with all permissions
        Set<UserPermission> allPerms = EnumSet.allOf(UserPermission.class);
        UserRole adminRole = new UserRole();
        adminRole.setId("ADMIN");
        adminRole.setPermissions(allPerms);
        roleRepo.save(adminRole);

        String password = "password123";
        // Create admin user
//        AppUser admin = new AppUser();
//        admin.setUsername("admin");
//        admin.setPassword(encoder.encode(password));
//        admin.setRole(adminRole);
//        userRepo.save(admin);
        UserCreationDto dto = new UserCreationDto();
        dto.setUsername("admin");
        dto.setPassword(password);
        dto.setRole(adminRole.getId());
        appUserService.create(dto);

        System.out.println("✅ Seeded admin user with username=admin and password=" + password);
    }
}
