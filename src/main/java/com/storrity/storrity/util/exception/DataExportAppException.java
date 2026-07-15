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
 **
 * Wraps any failure encountered while generating a product export file.
 * Export writes to an in-memory buffer, so this should be rare in practice
 * (e.g. it's not something a caller can fix by changing their request) —
 * hence 500 rather than 400.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DataExportAppException extends RuntimeException {

    public DataExportAppException(String message) {
        super(message);
    }

    public DataExportAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
