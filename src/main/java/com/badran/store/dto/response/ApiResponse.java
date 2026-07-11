package com.badran.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard success response envelope returned by REST controllers.
 *
 * @param <T> response payload type
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API response envelope.")
public class ApiResponse<T> {
    @Schema(description = "Indicates whether the operation succeeded.", example = "true")
    private boolean success;

    @Schema(description = "Human-readable operation message.", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Response payload.")
    private T data;

    @Builder.Default
    @Schema(description = "Response creation timestamp.", example = "2026-07-08T20:30:00")
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Builds a success envelope with a payload and message.
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Builds a success envelope with the default success message.
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation completed successfully");
    }

}
