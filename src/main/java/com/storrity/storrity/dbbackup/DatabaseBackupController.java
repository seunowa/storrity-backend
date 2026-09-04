/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.dbbackup;

import com.storrity.storrity.util.exception.AuthorizationError;
import com.storrity.storrity.util.exception.ServerError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/system/database")
@Tag(
        name = "Database",
        description = "Operations related to database management"
)
public class DatabaseBackupController {

    private final DatabaseBackupService backupService;

    @Autowired
    public DatabaseBackupController(DatabaseBackupService backupService) {
        this.backupService = backupService;
    }

    @Operation(
            operationId = "backupDatabase",
            description = "Create a backup of the Storrity PostgreSQL database",
            summary = "Backup database",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Database backup created successfully",
            content = @Content(
                schema = @Schema(implementation = DatabaseBackupService.BackupResult.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication Error",
            content = @Content(
                schema = @Schema(implementation = AuthorizationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Database backup failed",
            content = @Content(
                schema = @Schema(implementation = ServerError.class)
            )
        )
    })
    @PostMapping("/backup")
    public ResponseEntity<DatabaseBackupService.BackupResult> backup() {

        try {
            DatabaseBackupService.BackupResult result = backupService.backup();

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Database backup failed",
                    e
            );
        }
    }
}