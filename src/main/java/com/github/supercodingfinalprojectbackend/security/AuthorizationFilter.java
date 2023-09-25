package com.github.supercodingfinalprojectbackend.security;

import com.github.supercodingfinalprojectbackend.util.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final ProviderManager providerManager;
    private final SecretKey secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = JwtUtils.getJwtFromRequest(request);

        if (jwt != null) {
            Authentication beforeAuthenticate = JwtUtils.parseAuthentication(jwt, secretKey);
            Authentication afterAuthenticate = providerManager.authenticate(beforeAuthenticate);
            SecurityContextHolder.getContext().setAuthentication(afterAuthenticate);
        }

        doFilter(request, response, filterChain);
    }
}
