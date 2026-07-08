package com.badran.store.security;

public record UserPrincipal(Long userId, String email, String role) {
}
