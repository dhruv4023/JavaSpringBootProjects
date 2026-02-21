package com.authserver.authserver.user.security;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.AntPathMatcher;

@Component
@RequiredArgsConstructor
public class DynamicAuthorizationFilter extends OncePerRequestFilter {

    private final List<String> excludeRoutes = List.of("/error", "/auth/signup", "/auth/login", "/auth/forgot-password",
            "/");
    AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {

            String requestURI = request.getRequestURI().startsWith("/api") ? request.getRequestURI()
                    .substring(4) : request.getRequestURI();

            if (excludeRoutes.contains(requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            boolean allowed = authorities.stream()
                    .anyMatch(auth -> {
                        if (auth.getAuthority().equals("ROLE_SUPER_USER")) {
                            return true;
                        }
                        Objects.requireNonNull(requestURI);
                        Objects.requireNonNull(auth.getAuthority());
                        return matcher.match(auth.getAuthority(), requestURI);
                    });

            if (!allowed) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Access Denied");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
