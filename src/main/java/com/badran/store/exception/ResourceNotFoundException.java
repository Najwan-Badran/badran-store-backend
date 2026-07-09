package com.badran.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for missing domain resources such as products, users, carts, or orders.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Creates a not-found exception with a client-safe message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
