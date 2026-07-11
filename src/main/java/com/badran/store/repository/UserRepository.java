package com.badran.store.repository;

import com.badran.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for user authentication, profile, and password-reset lookups.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by email for login and account checks.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks whether an email is already registered.
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by password reset token.
     */
    Optional<User> findByPasswordResetToken(UUID token);
}
