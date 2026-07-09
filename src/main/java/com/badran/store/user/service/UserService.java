package com.badran.store.user.service;

import com.badran.store.auth.dto.*;
import com.badran.store.user.dto.UserDto;

/**
 * Application service contract for user registration, authentication, profile lookup, and password reset.
 */
public interface UserService {
    /**
     * Registers a new customer account.
     */
    UserDto register(RegisterRequest request);

    /**
     * Authenticates a user and returns a JWT response.
     */
    LoginResponse login(LoginRequest request);

    /**
     * Retrieves a user profile by identifier.
     */
    UserDto getUserById(Long userId);

    /**
     * Creates a password reset token for the supplied email address.
     */
    String initiatePasswordReset(String email);

    /**
     * Resets a password using a valid reset token.
     */
    void resetPassword(ResetPasswordRequest request);
}
