/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.licernse.controller;

/**
 *
 * @author Seun Owa
 */

import com.storrity.storrity.license.dto.IsAddClientSystemAllowedDto;
import com.storrity.storrity.license.dto.IsClientSystemLicensedDto;
import com.storrity.storrity.license.dto.LicenseDto;
import com.storrity.storrity.license.service.LicenseJwtUtil;
import com.storrity.storrity.license.service.LicenseService;
import com.storrity.storrity.license.service.MachineIdentifier;
import com.storrity.storrity.util.exception.ApiError;
import com.storrity.storrity.util.exception.AuthenticationError;
import com.storrity.storrity.util.exception.ServerError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/license")
@Tag(name = "License", description = "Operations related to license management")
public class LicenseController {
    
    private final LicenseService licenseService;
    

    @Autowired
    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;      
    }
    
    @Operation(
            operationId = "createLicense",
            description = "Create a license",
            summary = "Create a license"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "License created successfully",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "token not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })    
    @GetMapping("new")
    public String create(){
        return licenseService.generateToken("sample");
    }
    
    @Operation(
            operationId = "getLicenseDetails",
            description = "Get details of license installed on system",
            summary = "Get details of license installed on system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Installed license details retrieved successfully",
            content = @Content(schema = @Schema(implementation = Integer.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "License not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })    
    @GetMapping
    public LicenseDto getInstalledLicenseDetails(){
//        String token = licenseService.fetchLicense();
        return licenseService.getInstalledLicenseDetails();
    }
    
    
    @Operation(
        operationId = "getSystemIdentifier",
        description = "Retrieve the unique hardware identifier of this system.",
        summary = "Get system identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "System identifier retrieved successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected error",
                    content = @Content(schema = @Schema(implementation = ServerError.class))
            )
    })
    @GetMapping("/identifier")
    public ResponseEntity<byte[]> getSystemIdentifier() {
        
//        String identifier = MachineIdentifier.getMachineUUID();
        String identifier = licenseService.getSystemIdentifier();

        String content = identifier + "\n";

        byte[] fileBytes = content.getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=system-identifier.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(fileBytes);
    }       
    
    @Operation(
            operationId = "importLicense",
            summary = "Import a license file",
            description = "Uploads and validates a license file and installs it into the system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "License imported successfully",
            content = @Content(schema = @Schema(implementation = LicenseDto.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Bad request",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LicenseDto importLicense(
            @Parameter(
                    description = "License file to upload",
                    required = true,
                    content = @Content(mediaType = "multipart/form-data")
            )
            @RequestParam("file") MultipartFile file) {
        return licenseService.importLicense(file);
    }       
    
    @Operation(
            operationId = "validateLicense",
            summary = "Validate a license file",
            description = "Uploads and validates a license file"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "License validated successfully",
            content = @Content(schema = @Schema(implementation = LicenseDto.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Bad request",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PostMapping(value = "/validate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LicenseDto validateLicense(
            @Parameter(
                    description = "License file to upload",
                    required = true,
                    content = @Content(mediaType = "multipart/form-data")
            )
            @RequestParam("file") MultipartFile file) {
        return licenseService.validateLicense(file);
    }
    
    @Operation(
            operationId = "isAddClientSystemAllowed",
            description = "Is add client system allowed",
            summary = "Is add client system allowed"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Add client system is allowed",
            content = @Content(schema = @Schema(implementation = IsAddClientSystemAllowedDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })    
    @GetMapping("is_add_client_system_allowed")
    public IsAddClientSystemAllowedDto isAddClientSystemAllowed(){
        return licenseService.isAddClientSystemAllowed();
    }
    
    @Operation(
            operationId = "isClientSystemLicensed",
            description = "Is client system licensed",
            summary = "Is client system licensed"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Client system is licensed",
            content = @Content(schema = @Schema(implementation = IsClientSystemLicensedDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),        
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })    
    @GetMapping("is_client_system_licensed/{clientId}")
    public IsClientSystemLicensedDto isClientSystemLicensed(@PathVariable(name = "clientId") String clientId){
        return licenseService.isClientSystemLicensed(clientId);
    }
}
