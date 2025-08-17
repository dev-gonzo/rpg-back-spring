package com.rpgsystem.rpg.infrastructure.config;

import com.rpgsystem.rpg.infrastructure.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Teste unitário para SecurityConfig focado em cobertura de métodos individuais
 * para alcançar 100% de cobertura de código.
 */
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "jwt.secret=dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tdGVzdGluZy1wdXJwb3Nlcy1vbmx5LXRoaXMtaXMtYS12ZXJ5LWxvbmctc2VjcmV0LWtleQ=="
})
@DisplayName("SecurityConfig Method Coverage Test")
class SecurityConfigMethodCoverageTest {

    private SecurityConfig securityConfig;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        // Create a real UserDetailsService for testing
        UserDetails testUser = User.builder()
            .username("testuser")
            .password("{noop}password")
            .roles("USER")
            .build();
        
        userDetailsService = new InMemoryUserDetailsManager(testUser);
        securityConfig = new SecurityConfig(userDetailsService);
        
        // Set the JWT secret using reflection for testing
        ReflectionTestUtils.setField(securityConfig, "jwtSecret", 
            "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tdGVzdGluZy1wdXJwb3Nlcy1vbmx5LXRoaXMtaXMtYS12ZXJ5LWxvbmctc2VjcmV0LWtleQ==");
    }

    @Test
    @DisplayName("Should create CORS configuration source")
    void shouldCreateCorsConfigurationSource() {
        // When
        CorsConfigurationSource corsSource = securityConfig.corsConfigurationSource();
        
        // Then
        assertNotNull(corsSource, "CORS configuration source should not be null");
        
        // We can't test getCorsConfiguration with null request in unit test
        // The important thing is that the bean is created successfully
        // The actual CORS configuration is tested in integration tests
    }

    @Test
    @DisplayName("Should create password encoder")
    void shouldCreatePasswordEncoder() {
        // When
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        // Then
        assertNotNull(passwordEncoder, "Password encoder should not be null");
        
        // Test encoding functionality
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertNotNull(encodedPassword, "Encoded password should not be null");
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "Password should match");
    }

    @Test
    @DisplayName("Should create JWT encoder")
    void shouldCreateJwtEncoder() {
        // When
        JwtEncoder jwtEncoder = securityConfig.jwtEncoder();
        
        // Then
        assertNotNull(jwtEncoder, "JWT encoder should not be null");
    }

    @Test
    @DisplayName("Should create JWT decoder")
    void shouldCreateJwtDecoder() {
        // When
        JwtDecoder jwtDecoder = securityConfig.jwtDecoder();
        
        // Then
        assertNotNull(jwtDecoder, "JWT decoder should not be null");
    }

    @Test
    @DisplayName("Should create authentication provider")
    void shouldCreateAuthenticationProvider() {
        // When
        AuthenticationProvider authProvider = securityConfig.authenticationProvider();
        
        // Then
        assertNotNull(authProvider, "Authentication provider should not be null");
    }

    @Test
    @DisplayName("Should create authentication manager")
    void shouldCreateAuthenticationManager() {
        // Given
        AuthenticationProvider authProvider = securityConfig.authenticationProvider();
        
        // When
        AuthenticationManager authManager = securityConfig.authenticationManager(authProvider);
        
        // Then
        assertNotNull(authManager, "Authentication manager should not be null");
        // AuthenticationManager is an interface, we can only verify it's not null
        // The actual implementation details are tested in integration tests
    }

    @Test
    @DisplayName("Should create JWT authentication filter")
    void shouldCreateJwtAuthenticationFilter() {
        // Given
        JwtDecoder jwtDecoder = securityConfig.jwtDecoder();
        
        // When
        JwtAuthenticationFilter jwtFilter = securityConfig.jwtAuthenticationFilter(jwtDecoder);
        
        // Then
        assertNotNull(jwtFilter, "JWT authentication filter should not be null");
    }

    @Test
    @DisplayName("Should verify SecurityConfig constructor")
    void shouldVerifySecurityConfigConstructor() {
        // Given
        UserDetailsService testUserDetailsService = new InMemoryUserDetailsManager(
            User.builder()
                .username("constructor-test")
                .password("{noop}password")
                .roles("USER")
                .build()
        );
        
        // When
        SecurityConfig newSecurityConfig = new SecurityConfig(testUserDetailsService);
        
        // Set JWT secret for the new instance
        ReflectionTestUtils.setField(newSecurityConfig, "jwtSecret", 
            "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tdGVzdGluZy1wdXJwb3Nlcy1vbmx5LXRoaXMtaXMtYS12ZXJ5LWxvbmctc2VjcmV0LWtleQ==");
        
        // Then
        assertNotNull(newSecurityConfig, "SecurityConfig should be created");
        
        // Verify that all bean creation methods work with the new instance
        assertNotNull(newSecurityConfig.corsConfigurationSource(), "CORS source should work");
        assertNotNull(newSecurityConfig.passwordEncoder(), "Password encoder should work");
        assertNotNull(newSecurityConfig.jwtEncoder(), "JWT encoder should work");
        assertNotNull(newSecurityConfig.jwtDecoder(), "JWT decoder should work");
        assertNotNull(newSecurityConfig.authenticationProvider(), "Auth provider should work");
    }

    @Test
    @DisplayName("Should verify all configuration methods are accessible")
    void shouldVerifyAllConfigurationMethodsAreAccessible() {
        // This test ensures all public methods are called at least once
        // to improve method coverage
        
        // Test all bean creation methods
        assertNotNull(securityConfig.corsConfigurationSource());
        assertNotNull(securityConfig.passwordEncoder());
        assertNotNull(securityConfig.jwtEncoder());
        assertNotNull(securityConfig.jwtDecoder());
        assertNotNull(securityConfig.authenticationProvider());
        
        // Test methods with parameters
        AuthenticationProvider provider = securityConfig.authenticationProvider();
        assertNotNull(securityConfig.authenticationManager(provider));
        
        JwtDecoder decoder = securityConfig.jwtDecoder();
        assertNotNull(securityConfig.jwtAuthenticationFilter(decoder));
        
        // Verify that methods can be called multiple times (idempotent)
        assertNotNull(securityConfig.corsConfigurationSource());
        assertNotNull(securityConfig.passwordEncoder());
    }
}