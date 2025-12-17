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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
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
@Schema(description = "Standard error response")
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerError {
    
    @Schema(example = "500")
    private int status;

    @Schema(example = "Internal Server Error")
    private String error;

    @Schema(example = "Something went wrong.")
    private String message;

    public ServerError(String message) {
        this.status = 500;
        this.error = "Internal Server Error";
        this.message = message;
    }
    
    
}
