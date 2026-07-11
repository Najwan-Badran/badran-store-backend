package com.badran.store.security;

import com.badran.store.entity.User;
import com.badran.store.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Request filter that authenticates bearer JWTs and enforces X-User-Id ownership consistency.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USER_ID_HEADER = "X-User-Id";

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());
        if (!jwtUtils.validateToken(token)) {
            log.warn("Rejected request with invalid JWT: {}", request.getRequestURI());
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long tokenUserId = jwtUtils.extractUserId(token);
        User user = userRepository.findById(tokenUserId)
                .filter(User::getIsActive)
                .orElse(null);
        if (user == null) {
            log.warn("Rejected JWT for missing or inactive user id={} path={}", tokenUserId, request.getRequestURI());
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String role = user.getRole().getRoleName();
        String requestedUserId = request.getHeader(USER_ID_HEADER);
        if (requestedUserId != null && !requestedUserId.isBlank() && !SecurityContextService.isAdminRole(role)) {
            try {
                if (!tokenUserId.equals(Long.valueOf(requestedUserId))) {
                    log.warn("Rejected user header mismatch tokenUserId={} requestedUserId={} path={}",
                            tokenUserId, requestedUserId, request.getRequestURI());
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            } catch (NumberFormatException ex) {
                log.warn("Rejected invalid X-User-Id header value={} path={}", requestedUserId, request.getRequestURI());
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        UserPrincipal principal = new UserPrincipal(user.getUserId(), user.getEmail(), role);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

}
