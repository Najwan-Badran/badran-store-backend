package com.badran.store.security;

/**
 * Authenticated user identity stored in Spring Security's context for authorization decisions.
 */
public record UserPrincipal(Long userId, String email, String role) {
}
