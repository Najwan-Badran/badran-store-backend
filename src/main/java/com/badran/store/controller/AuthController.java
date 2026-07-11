package com.badran.store.controller;

import com.badran.store.dto.response.ApiResponse;
import com.badran.store.dto.request.LoginRequest;
import com.badran.store.dto.response.LoginResponse;
import com.badran.store.dto.request.RegisterRequest;
import com.badran.store.dto.request.ResetPasswordRequest;
import com.badran.store.dto.model.UserDto;
import com.badran.store.security.SecurityContextService;
import com.badran.store.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for registration, login, profile, and password reset operations.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "Registration, JWT login, profile, and password reset endpoints.")
public class AuthController {

    private final UserService userService;
    private final SecurityContextService securityContextService;

    /**
     * Registers a new customer account.
     */
    @Operation(
            summary = "Register customer",
            description = "Creates a customer account and returns the registered user profile.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registration successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed or email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody RegisterRequest request) {
        UserDto userDto = userService.register(request);
        return ResponseEntity.ok(ApiResponse.success(userDto, "Registration successful"));
    }

    /**
     * Authenticates a user and returns a JWT response.
     */
    @Operation(
            summary = "Login",
            description = "Authenticates a registered user and returns a JWT bearer token.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    /**
     * Returns the authenticated user's profile by the validated user id header.
     */
    @Operation(
            summary = "Get profile",
            description = "Returns the authenticated user's profile. The X-User-Id header must match the JWT user unless the caller is an admin.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "JWT is missing or invalid"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "X-User-Id does not match the authenticated user")
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getProfile(@RequestHeader("X-User-Id") @Positive Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(userDto, "Profile retrieved successfully"));
    }

    /**
     * Generates a password reset token for the authenticated user's own email, or for any user when admin.
     */
    @Operation(
            summary = "Request password reset",
            description = "Generates a password reset token for the authenticated user's email. Admin users can request resets for any user.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset token generated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Cannot request a reset for another user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Email does not exist")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @RequestParam @NotBlank @Email @Size(max = 255) String email) {
        securityContextService.assertAdminOrOwnEmail(email);
        String token = userService.initiatePasswordReset(email);
        return ResponseEntity.ok(ApiResponse.success(token, "Password reset token generated (simulated email sending)"));
    }

    /**
     * Resets a password with a valid reset token.
     */
    @Operation(
            summary = "Reset password",
            description = "Updates the password associated with a valid password reset token.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid, expired, or malformed reset token")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successful"));
    }
}
