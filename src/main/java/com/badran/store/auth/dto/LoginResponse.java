package com.badran.store.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT login response returned after successful authentication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authenticated user identity and bearer token.")
public class LoginResponse {
    @Schema(description = "JWT bearer token.", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Authenticated user's email.", example = "admin@badranstore.ps")
    private String email;

    @Schema(description = "Authenticated user's display name.", example = "Store Admin")
    private String name;

    @Schema(description = "Authenticated user's role.", example = "admin")
    private String role;
}
