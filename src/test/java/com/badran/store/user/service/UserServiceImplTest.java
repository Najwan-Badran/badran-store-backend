package com.badran.store.user.service;

import com.badran.store.auth.dto.RegisterRequest;
import com.badran.store.auth.dto.ResetPasswordRequest;
import com.badran.store.exception.BadRequestException;
import com.badran.store.security.JwtUtils;
import com.badran.store.user.entity.Role;
import com.badran.store.user.entity.User;
import com.badran.store.user.mapper.UserMapper;
import com.badran.store.user.repository.RoleRepository;
import com.badran.store.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void registerRejectsDuplicateEmailBeforeHashingPassword() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setPassword("secret123");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email is already in use");

        verify(passwordEncoder, never()).encode(org.mockito.ArgumentMatchers.anyString());
        verify(roleRepository, never()).findByRoleName(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void resetPasswordRejectsExpiredTokenWithoutUpdatingPassword() {
        UUID token = UUID.randomUUID();
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken(token.toString());
        request.setNewPassword("newSecret123");

        User user = User.builder()
                .role(Role.builder().roleName("customer").build())
                .passwordResetToken(token)
                .passwordResetExpiresAt(OffsetDateTime.now().minusMinutes(1))
                .build();

        when(userRepository.findByPasswordResetToken(token)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.resetPassword(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("expired");

        verify(passwordEncoder, never()).encode(org.mockito.ArgumentMatchers.anyString());
        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
