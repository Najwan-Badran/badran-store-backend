package com.badran.store.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Customer registration payload used to create a new account.
 */
@Data
@Schema(description = "Customer registration details.")
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Schema(description = "Customer full name.", example = "Ahmad Khalil")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Unique customer email.", example = "ahmad.khalil@example.com")
    private String email;

    @Schema(description = "Customer phone number.", example = "+970599123456")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Schema(description = "Customer password with at least six characters.", example = "password123")
    private String password;

    @Schema(description = "Preferred language code for localized content.", example = "en")
    private String preferredLanguage = "en"; // Default
}
