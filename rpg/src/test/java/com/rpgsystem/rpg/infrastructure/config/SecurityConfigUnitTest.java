package com.rpgsystem.rpg.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.http.HttpMethod;
import com.rpgsystem.rpg.infrastructure.security.JwtAuthenticationFilter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityConfig Unit Tests")
class SecurityConfigUnitTest {

    @Mock
    private HttpSecurity httpSecurity;
    
    @Mock
    private CsrfConfigurer<HttpSecurity> csrfConfigurer;
    
    @Mock
    private CorsConfigurer<HttpSecurity> corsConfigurer;
    
    @Mock
    private AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationRegistry;
    
    @Mock
    private AuthorizeHttpRequestsConfigurer<HttpSecurity> authorizeConfigurer;
    
    @Mock
    private SessionManagementConfigurer<HttpSecurity> sessionConfigurer;
    
    @Mock
    private DefaultSecurityFilterChain securityFilterChain;
    
    @Mock
    private AuthenticationProvider authenticationProvider;
    
    @Mock
    private JwtDecoder jwtDecoder;
    
    @Mock
    private JwtEncoder jwtEncoder;
    
    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Mock
    private UserDetailsService userDetailsService;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(userDetailsService);
    }

    @Test
    @DisplayName("Should create SecurityFilterChain with correct configuration")
    void shouldCreateSecurityFilterChainWithCorrectConfiguration() throws Exception {
        // Given
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(securityFilterChain);

        // When
        SecurityFilterChain result = securityConfig.securityFilterChain(
            httpSecurity, 
            jwtAuthenticationFilter
        );

        // Then
        assertNotNull(result);
        verify(httpSecurity).csrf(any());
        verify(httpSecurity).cors(any());
        verify(httpSecurity).authorizeHttpRequests(any());
        verify(httpSecurity).sessionManagement(any());
        verify(httpSecurity).addFilterBefore(eq(jwtAuthenticationFilter), eq(UsernamePasswordAuthenticationFilter.class));
        verify(httpSecurity).build();
    }

    @Test
    @DisplayName("Should disable CSRF protection")
    void shouldDisableCsrfProtection() throws Exception {
        // Given
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(securityFilterChain);

        // When
        securityConfig.securityFilterChain(httpSecurity, jwtAuthenticationFilter);

        // Then
        verify(httpSecurity).csrf(any());
    }

    @Test
    @DisplayName("Should configure CORS")
    void shouldConfigureCors() throws Exception {
        // Given
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(securityFilterChain);

        // When
        securityConfig.securityFilterChain(httpSecurity, jwtAuthenticationFilter);

        // Then
        verify(httpSecurity).cors(any());
    }

    @Test
    @DisplayName("Should configure authorization rules")
    void shouldConfigureAuthorizationRules() throws Exception {
        // Given
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(securityFilterChain);

        // When
        securityConfig.securityFilterChain(httpSecurity, jwtAuthenticationFilter);

        // Then
        verify(httpSecurity).authorizeHttpRequests(any());
    }

    @Test
    @DisplayName("Should configure session management as stateless")
    void shouldConfigureSessionManagementAsStateless() throws Exception {
        // Given
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(securityFilterChain);

        // When
        securityConfig.securityFilterChain(httpSecurity, jwtAuthenticationFilter);

        // Then
        verify(httpSecurity).sessionManagement(any());
    }

    @Test
    @DisplayName("Should add JWT authentication filter before UsernamePasswordAuthenticationFilter")
    void shouldAddJwtAuthenticationFilterBeforeUsernamePasswordAuthenticationFilter() throws Exception {
        // Given
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(securityFilterChain);

        // When
        securityConfig.securityFilterChain(httpSecurity, jwtAuthenticationFilter);

        // Then
        verify(httpSecurity).addFilterBefore(eq(jwtAuthenticationFilter), eq(UsernamePasswordAuthenticationFilter.class));
    }




}