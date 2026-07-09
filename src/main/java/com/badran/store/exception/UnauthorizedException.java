package com.badran.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for authentication failures that should return HTTP 401.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    /**
     * Creates an unauthorized exception with a client-safe message.
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
