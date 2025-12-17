/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.util.exception;

import java.util.Map;
import java.util.stream.Collectors;
import javax.security.sasl.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author Seun Owa
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationError handleValidationException(MethodArgumentNotValidException ex) {
        System.err.println(ex);
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1)); // Resolve duplicates

//        return ValidationError.builder()
//            .status(400)
//            .error("Bad Request")
//            .message("Validation failed")
//            .fieldErrors(errors)
//            .build();
           return new ValidationError(errors);
    }
    
    @ExceptionHandler(ResourceAlreadyExistsAppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleResourceAlreadyExists(ResourceAlreadyExistsAppException ex) {
        System.err.println(ex);
        return new ApiError(400, "Resource already exists", ex.getMessage());
    }
    
    @ExceptionHandler(BadRequestAppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(BadRequestAppException ex) {
        System.err.println(ex);
        return new ApiError(400, "Bad request", ex.getMessage());
    }
    
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIllegalStateException(IllegalStateException ex) {
        System.err.println(ex);
        return new ApiError(400, "Illegal state exception", ex.getMessage());
    }
    
    @ExceptionHandler(ResourceNotFoundAppException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleResourceNotFound(ResourceNotFoundAppException ex) {
        System.err.println(ex);
        return new ApiError(404, "Not Found", ex.getMessage());
    }
    
//    @Todo
// handleAuthorizationException below is not working as Spring security filter chain interceps the flow before it reaches this point
//    to customize the paylod on response implement CustomAccessDeniedHandler implements AccessDeniedHandler
//    I have ingored this issue as it is not priority at the moment
    @ExceptionHandler(AuthenticationException.class)//the exception class here appears to be the wrong exception class to trap
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public AuthorizationError handleAuthorizationException(AuthenticationException ex) {
        System.err.println(ex);
        String message = "Access denied - you do not have permission to access this resource";
        return new AuthorizationError(message);
    }    
    
    @ExceptionHandler(AuthenticationAppException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthenticationError handleAuthenticationException(AuthenticationAppException ex) {
        System.err.println(ex);
        return new AuthenticationError(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServerError handleGenericException(Exception ex) {
        ex.printStackTrace();
        String message = "Something went wrong.";
        return new ServerError(message);
//        return new ApiError(500, "Internal Server Error", "Something went wrong.");
    }
}
