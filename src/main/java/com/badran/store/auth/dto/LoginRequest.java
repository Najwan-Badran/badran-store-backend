package com.badran.store.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Login payload used to authenticate a user and issue a JWT.
 */
@Data
@Schema(description = "Credentials submitted to authenticate a registered user.")
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Registered account email.", example = "admin@badranstore.ps")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Account password.", example = "password123")
    private String password;
}
