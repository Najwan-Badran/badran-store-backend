package com.badran.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid client requests such as failed validation, unsupported options, or stock conflicts.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    /**
     * Creates a bad-request exception with a client-safe message.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
