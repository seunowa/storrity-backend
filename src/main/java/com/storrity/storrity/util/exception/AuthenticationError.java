/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.util.exception;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
/**
 *
 * @author Seun Owa
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Schema(description = "Autnentication error response")
public class AuthenticationError {
    
    @Schema(example = "403")
    private int status;

    @Schema(example = "Authentication error")
    private String error;

    @Schema(example = "Access denied - you do not have permission to access this resource")
    private String message;

    public AuthenticationError(String message) {
        this.status = 403;
        this.error = "Authentication error";
        this.message = message;
    }
    
    
}
