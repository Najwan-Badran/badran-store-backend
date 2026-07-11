package com.badran.store.service.impl;

import com.badran.store.dto.request.LoginRequest;
import com.badran.store.dto.response.LoginResponse;
import com.badran.store.dto.request.RegisterRequest;
import com.badran.store.dto.request.ResetPasswordRequest;
import com.badran.store.dto.model.UserDto;
import com.badran.store.entity.Role;
import com.badran.store.entity.User;
import com.badran.store.enums.UserRole;
import com.badran.store.mapper.UserMapper;
import com.badran.store.repository.RoleRepository;
import com.badran.store.repository.UserRepository;
import com.badran.store.exception.BadRequestException;
import com.badran.store.exception.ResourceNotFoundException;
import com.badran.store.security.JwtUtils;
import com.badran.store.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Default user service implementation for registration, authentication, profile lookup, and password reset workflows.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    /** {@inheritDoc} */
    @Override
    @Transactional
    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration rejected because email is already registered: {}", request.getEmail());
            throw new BadRequestException("Email is already in use");
        }

        // Fetch customer role (ID = 1)
        Role customerRole = roleRepository.findByRoleName(UserRole.CUSTOMER.value())
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
        log.info("Registered user id={} email={}", savedUser.getUserId(), savedUser.getEmail());
        return userMapper.toDto(savedUser);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed for unknown email={}", request.getEmail());
                    return new BadRequestException("Invalid email or password");
                });

        if (!user.getIsActive()) {
            log.warn("Login rejected for inactive user id={} email={}", user.getUserId(), user.getEmail());
            throw new BadRequestException("User account is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("Login failed due to invalid password for user id={} email={}", user.getUserId(), user.getEmail());
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtUtils.generateToken(user.getEmail(), user.getUserId(), user.getRole().getRoleName());
        log.info("Login succeeded for user id={} email={}", user.getUserId(), user.getEmail());

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().getRoleName())
                .build();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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
