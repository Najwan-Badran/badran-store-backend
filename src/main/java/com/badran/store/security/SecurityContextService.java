package com.badran.store.security;

import com.badran.store.enums.UserRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * Small adapter around Spring Security's context for controller-level ownership checks.
 */
@Component
public class SecurityContextService {

    /**
     * Returns the current authenticated application principal when present.
     */
    public Optional<UserPrincipal> currentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
            return Optional.of(principal);
        }
        return Optional.empty();
    }

    /**
     * Returns true when the current principal has the admin role.
     */
    public boolean isCurrentUserAdmin() {
        return currentPrincipal()
                .map(UserPrincipal::role)
                .map(SecurityContextService::isAdminRole)
                .orElse(false);
    }

    /**
     * Returns true when a role name represents an administrator.
     */
    public static boolean isAdminRole(String role) {
        return UserRole.ADMIN.value().equalsIgnoreCase(role);
    }

    /**
     * Requires the current principal to be an admin or to match the supplied owner id.
     */
    public void assertAdminOrOwner(Long requestedUserId, Long ownerUserId, String message) {
        if (isCurrentUserAdmin()) {
            return;
        }
        if (!Objects.equals(requestedUserId, ownerUserId)) {
            throw new AccessDeniedException(message);
        }
    }

    /**
     * Requires the current principal to be an admin or to own the supplied email address.
     */
    public void assertAdminOrOwnEmail(String email) {
        currentPrincipal()
                .filter(principal -> isAdminRole(principal.role()) || principal.email().equalsIgnoreCase(email))
                .orElseThrow(() -> new AccessDeniedException("Cannot request password reset for another user"));
    }
}
