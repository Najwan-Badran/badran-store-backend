package com.badran.store.user.service;

import com.badran.store.auth.dto.*;
import com.badran.store.user.dto.UserDto;

public interface UserService {
    UserDto register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    UserDto getUserById(Long userId);
    String initiatePasswordReset(String email);
    void resetPassword(ResetPasswordRequest request);
}
