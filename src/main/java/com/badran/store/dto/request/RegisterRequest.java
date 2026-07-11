package com.badran.store.dto.request;

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
    @Size(max = 255, message = "Name must not exceed 255 characters")
    @Schema(description = "Customer full name.", example = "Ahmad Khalil")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Schema(description = "Unique customer email.", example = "ahmad.khalil@example.com")
    private String email;

    @Size(max = 255, message = "Phone must not exceed 255 characters")
    @Schema(description = "Customer phone number.", example = "+970599123456")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters long")
    @Schema(description = "Customer password with at least six characters.", example = "password123")
    private String password;

    @Size(max = 20, message = "Preferred language must not exceed 20 characters")
    @Schema(description = "Preferred language code for localized content.", example = "en")
    private String preferredLanguage = "en"; // Default
}
