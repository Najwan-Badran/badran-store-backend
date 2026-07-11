package com.badran.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response envelope returned by exception handlers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API error response envelope.")
public class ErrorResponse {
    @Schema(description = "Always false for error responses.", example = "false")
    private boolean success;

    @Schema(description = "HTTP status code.", example = "400")
    private int status;

    @Schema(description = "HTTP status reason phrase.", example = "Bad Request")
    private String error;

    @Schema(description = "Human-readable error message.", example = "Validation failed")
    private String message;

    @Schema(description = "Field-level validation errors keyed by field name.")
    private Map<String, String> errors;

    @Schema(description = "Request path that produced the error.", example = "/api/v1/auth/register")
    private String path;

    @Builder.Default
    @Schema(description = "Error response timestamp.", example = "2026-07-08T20:30:00")
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Builds an error response with no field-level details.
     */
    public static ErrorResponse of(String message) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .build();
    }

    /**
     * Builds an error response with field-level validation details.
     */
    public static ErrorResponse of(String message, Map<String, String> errors) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .errors(errors)
                .build();
    }

    /**
     * Builds a complete error response with HTTP status metadata and request path.
     */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return ErrorResponse.builder()
                .success(false)
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }

    /**
     * Builds a complete validation error response with field-level details.
     */
    public static ErrorResponse of(int status, String error, String message, String path, Map<String, String> errors) {
        return ErrorResponse.builder()
                .success(false)
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .errors(errors)
                .build();
    }
}
