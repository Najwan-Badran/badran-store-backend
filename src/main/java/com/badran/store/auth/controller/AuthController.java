package com.badran.store.auth.controller;

import com.badran.store.auth.dto.*;
import com.badran.store.common.dto.ApiResponse;
import com.badran.store.security.UserPrincipal;
import com.badran.store.user.dto.UserDto;
import com.badran.store.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody RegisterRequest request) {
        UserDto userDto = userService.register(request);
        return ResponseEntity.ok(ApiResponse.success(userDto, "Registration successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getProfile(@RequestHeader("X-User-Id") Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(userDto, "Profile retrieved successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam String email) {
        assertCanRequestPasswordReset(email);
        String token = userService.initiatePasswordReset(email);
        return ResponseEntity.ok(ApiResponse.success(token, "Password reset token generated (simulated email sending)"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successful"));
    }

    private void assertCanRequestPasswordReset(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
            if ("admin".equalsIgnoreCase(principal.role()) || principal.email().equalsIgnoreCase(email)) {
                return;
            }
        }
        throw new AccessDeniedException("Cannot request password reset for another user");
    }
}
