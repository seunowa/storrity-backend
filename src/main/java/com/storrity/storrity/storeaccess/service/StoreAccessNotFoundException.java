package com.storrity.storrity.storeaccess.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StoreAccessNotFoundException extends RuntimeException {
    public StoreAccessNotFoundException(String message) {
        super(message);
    }
}
