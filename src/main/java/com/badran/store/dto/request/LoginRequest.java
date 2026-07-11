package com.badran.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Login payload used to authenticate a user and issue a JWT.
 */
@Data
@Schema(description = "Credentials submitted to authenticate a registered user.")
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Schema(description = "Registered account email.", example = "admin@badranstore.ps")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(max = 255, message = "Password must not exceed 255 characters")
    @Schema(description = "Account password.", example = "password123")
    private String password;
}
