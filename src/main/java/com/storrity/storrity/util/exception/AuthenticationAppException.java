/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Seun Owa
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationAppException extends RuntimeException {
    public AuthenticationAppException(String message) {
        super(message);
    }
}
