/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.util.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationError {
    
    @Schema(example = "400")
    private int status;

    @Schema(example = "Bad Request")
    private String error;

    @Schema(example = "Validation failed")
    private String message;
    
    @Schema(description = "Details of validation failures", example = "{\"fieldName\": \"must not be blank\"}")
    private Map<String, String> fieldErrors;

    public ValidationError(Map<String, String> fieldErrors) {
        this.status = 400;
        this.error = "Bad Request";
        this.message = "Validation failed";
        this.fieldErrors = fieldErrors;
    }
    
    
}
