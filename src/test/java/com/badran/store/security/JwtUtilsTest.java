package com.badran.store.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilsTest {

    @Test
    void generateTokenCreatesValidTokenWithExpectedClaims() {
        JwtUtils jwtUtils = configuredJwtUtils("test-secret-key-for-jwt-utils-test-32-bytes-minimum", 60_000L);

        String token = jwtUtils.generateToken("customer@example.com", 42L, "customer");

        assertThat(jwtUtils.validateToken(token)).isTrue();
        assertThat(jwtUtils.extractUsername(token)).isEqualTo("customer@example.com");
        assertThat(jwtUtils.extractUserId(token)).isEqualTo(42L);
        assertThat(jwtUtils.extractRole(token)).isEqualTo("customer");
    }

    @Test
    void validateConfigurationRejectsWeakSecret() {
        JwtUtils jwtUtils = configuredJwtUtils("short", 60_000L);

        assertThatThrownBy(jwtUtils::validateConfiguration)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("JWT secret");
    }

    private JwtUtils configuredJwtUtils(String secret, long expiration) {
        JwtUtils jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secret", secret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", expiration);
        return jwtUtils;
    }
}
