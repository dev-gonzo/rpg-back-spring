package com.rpgsystem.rpg.infrastructure.config;

import com.rpgsystem.rpg.infrastructure.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("SecurityConfig Direct Tests")
class SecurityConfigDirectTest {

    @Test
    @DisplayName("Should create SecurityConfig instance")
    void shouldCreateSecurityConfigInstance() {
        // Given
        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        
        // When
        SecurityConfig securityConfig = new SecurityConfig(mockUserDetailsService);
        
        // Then
        assertNotNull(securityConfig);
    }

    @Test
    @DisplayName("Should create security filter chain with basic configuration")
    void shouldCreateSecurityFilterChainWithBasicConfiguration() throws Exception {
        // Given
        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        SecurityConfig securityConfig = new SecurityConfig(mockUserDetailsService);
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        JwtAuthenticationFilter jwtFilter = mock(JwtAuthenticationFilter.class);
        DefaultSecurityFilterChain mockFilterChain = mock(DefaultSecurityFilterChain.class);
        
        // Configure mocks to return themselves for method chaining
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mockFilterChain);
        
        // When
        SecurityFilterChain result = securityConfig.securityFilterChain(httpSecurity, jwtFilter);
        
        // Then
        assertNotNull(result);
        verify(httpSecurity).csrf(any());
        verify(httpSecurity).cors(any());
        verify(httpSecurity).authorizeHttpRequests(any());
        verify(httpSecurity).sessionManagement(any());
        verify(httpSecurity).addFilterBefore(any(), any());
        verify(httpSecurity).build();
    }

    @Test
    @DisplayName("Should verify CSRF configuration is called")
    void shouldVerifyCsrfConfigurationIsCalled() throws Exception {
        // Given
        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        SecurityConfig securityConfig = new SecurityConfig(mockUserDetailsService);
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        JwtAuthenticationFilter jwtFilter = mock(JwtAuthenticationFilter.class);
        DefaultSecurityFilterChain mockFilterChain = mock(DefaultSecurityFilterChain.class);
        
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mockFilterChain);
        
        // When
        securityConfig.securityFilterChain(httpSecurity, jwtFilter);
        
        // Then
        verify(httpSecurity, times(1)).csrf(any());
    }

    @Test
    @DisplayName("Should verify CORS configuration is called")
    void shouldVerifyCorsConfigurationIsCalled() throws Exception {
        // Given
        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        SecurityConfig securityConfig = new SecurityConfig(mockUserDetailsService);
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        JwtAuthenticationFilter jwtFilter = mock(JwtAuthenticationFilter.class);
        DefaultSecurityFilterChain mockFilterChain = mock(DefaultSecurityFilterChain.class);
        
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mockFilterChain);
        
        // When
        securityConfig.securityFilterChain(httpSecurity, jwtFilter);
        
        // Then
        verify(httpSecurity, times(1)).cors(any());
    }

    @Test
    @DisplayName("Should verify authorization configuration is called")
    void shouldVerifyAuthorizationConfigurationIsCalled() throws Exception {
        // Given
        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        SecurityConfig securityConfig = new SecurityConfig(mockUserDetailsService);
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        JwtAuthenticationFilter jwtFilter = mock(JwtAuthenticationFilter.class);
        DefaultSecurityFilterChain mockFilterChain = mock(DefaultSecurityFilterChain.class);
        
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mockFilterChain);
        
        // When
        securityConfig.securityFilterChain(httpSecurity, jwtFilter);
        
        // Then
        verify(httpSecurity, times(1)).authorizeHttpRequests(any());
    }

    @Test
    @DisplayName("Should verify session management configuration is called")
    void shouldVerifySessionManagementConfigurationIsCalled() throws Exception {
        // Given
        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        SecurityConfig securityConfig = new SecurityConfig(mockUserDetailsService);
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        JwtAuthenticationFilter jwtFilter = mock(JwtAuthenticationFilter.class);
        DefaultSecurityFilterChain mockFilterChain = mock(DefaultSecurityFilterChain.class);
        
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mockFilterChain);
        
        // When
        securityConfig.securityFilterChain(httpSecurity, jwtFilter);
        
        // Then
        verify(httpSecurity, times(1)).sessionManagement(any());
    }
}