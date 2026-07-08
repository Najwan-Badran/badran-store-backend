package com.badran.store.user.service;

import com.badran.store.auth.dto.*;
import com.badran.store.user.entity.Role;
import com.badran.store.user.entity.User;
import com.badran.store.user.dto.UserDto;
import com.badran.store.user.mapper.UserMapper;
import com.badran.store.user.repository.RoleRepository;
import com.badran.store.user.repository.UserRepository;
import com.badran.store.exception.BadRequestException;
import com.badran.store.exception.ResourceNotFoundException;
import com.badran.store.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        // Fetch customer role (ID = 1)
        Role customerRole = roleRepository.findByRoleName("customer")
                .orElseThrow(() -> new ResourceNotFoundException("Default role 'customer' not found in database"));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .preferredLanguage(request.getPreferredLanguage())
                .role(customerRole)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!user.getIsActive()) {
            throw new BadRequestException("User account is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtUtils.generateToken(user.getEmail(), user.getUserId(), user.getRole().getRoleName());

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().getRoleName())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public String initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with this email does not exist"));

        UUID resetToken = UUID.randomUUID();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetExpiresAt(OffsetDateTime.now().plusHours(1)); // 1 hour expiry

        userRepository.save(user);

        // In a real production system, send an email here.
        // We will just return the token for this university project demonstration.
        return resetToken.toString();
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        UUID tokenUuid;
        try {
            tokenUuid = UUID.fromString(request.getToken());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid reset token format");
        }

        User user = userRepository.findByPasswordResetToken(tokenUuid)
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        if (user.getPasswordResetExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new BadRequestException("Reset token has expired");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);

        userRepository.save(user);
    }
}
