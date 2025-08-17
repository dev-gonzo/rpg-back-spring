package com.rpgsystem.rpg.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationFilter Tests")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Jwt jwt;

    @Mock
    private UserDetails userDetails;

    @Mock
    private SecurityContext securityContext;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtDecoder, userDetailsService);
    }

    @Test
    @DisplayName("Should skip authentication for login path")
    void shouldSkipAuthenticationForLoginPath() throws ServletException, IOException {
        // Given
        when(request.getRequestURI()).thenReturn("/auth/login");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtDecoder, userDetailsService);
    }

    @Test
    @DisplayName("Should skip authentication when no authorization header")
    void shouldSkipAuthenticationWhenNoAuthorizationHeader() throws ServletException, IOException {
        // Given
        when(request.getRequestURI()).thenReturn("/api/characters");
        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtDecoder, userDetailsService);
    }

    @Test
    @DisplayName("Should skip authentication when authorization header doesn't start with Bearer")
    void shouldSkipAuthenticationWhenAuthorizationHeaderDoesntStartWithBearer() throws ServletException, IOException {
        // Given
        when(request.getRequestURI()).thenReturn("/api/characters");
        when(request.getHeader("Authorization")).thenReturn("Basic dGVzdDp0ZXN0");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtDecoder, userDetailsService);
    }

    @Test
    @DisplayName("Should authenticate user with valid JWT token")
    void shouldAuthenticateUserWithValidJwtToken() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String username = "test@example.com";
        
        when(request.getRequestURI()).thenReturn("/api/characters");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtDecoder.decode(token)).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(jwtDecoder).decode(token);
            verify(userDetailsService).loadUserByUsername(username);
            verify(securityContext).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    @DisplayName("Should handle JWT decoding exception")
    void shouldHandleJwtDecodingException() throws ServletException, IOException {
        // Given
        String token = "invalid.jwt.token";
        
        when(request.getRequestURI()).thenReturn("/api/characters");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtDecoder.decode(token)).thenThrow(new JwtException("Invalid token"));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(jwtDecoder).decode(token);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(userDetailsService, filterChain);
    }

    @Test
    @DisplayName("Should skip authentication when user already authenticated")
    void shouldSkipAuthenticationWhenUserAlreadyAuthenticated() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String username = "test@example.com";
        UsernamePasswordAuthenticationToken existingAuth = new UsernamePasswordAuthenticationToken(
                "user", null, Collections.emptyList());
        
        when(request.getRequestURI()).thenReturn("/api/characters");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtDecoder.decode(token)).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(username);

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(existingAuth);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(jwtDecoder).decode(token);
            verifyNoInteractions(userDetailsService);
            verify(securityContext, never()).setAuthentication(any());
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    @DisplayName("Should handle null username from JWT")
    void shouldHandleNullUsernameFromJwt() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        
        when(request.getRequestURI()).thenReturn("/api/characters");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtDecoder.decode(token)).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(null);

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(jwtDecoder).decode(token);
            verifyNoInteractions(userDetailsService);
            verify(securityContext, never()).setAuthentication(any());
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    @DisplayName("Should extract token correctly from Bearer header")
    void shouldExtractTokenCorrectlyFromBearerHeader() throws ServletException, IOException {
        // Given
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String bearerHeader = "Bearer " + token;
        String username = "test@example.com";
        
        when(request.getRequestURI()).thenReturn("/api/characters");
        when(request.getHeader("Authorization")).thenReturn(bearerHeader);
        when(jwtDecoder.decode(token)).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(null);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(jwtDecoder).decode(eq(token)); // Verify exact token extraction
            verify(filterChain).doFilter(request, response);
        }
    }
}