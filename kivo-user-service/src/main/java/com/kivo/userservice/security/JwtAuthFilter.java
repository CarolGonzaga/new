package com.kivo.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (auth == null || !auth.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = auth.substring(7);

        try {
            String subject = jwtService.extractSubject(token);
            String role = jwtService.extractRole(token);
            Long userId = jwtService.extractUserId(token);

            if (subject != null && role != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String normalizedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                var authorities = List.of(new SimpleGrantedAuthority(normalizedRole));

                var authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);
                authentication.setDetails(userId);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ignored) {
        }

        filterChain.doFilter(request, response);
    }
}
