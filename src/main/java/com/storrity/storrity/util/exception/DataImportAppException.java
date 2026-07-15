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
 *
 *
 * Wraps any failure encountered while importing product data (I/O errors reading
 * the CSV, malformed file, etc). Kept distinct from InputValidationAppException /
 * ResourceNotFoundAppException so import failures are easy to identify and handle
 * separately on the client (e.g. show "couldn't read that file" vs a per-row
 * validation message, which is instead reported via ProductImportResultDto).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataImportAppException extends RuntimeException {

    public DataImportAppException(String message) {
        super(message);
    }

    public DataImportAppException(String message, Throwable cause) {
        super(message, cause);
    }
}