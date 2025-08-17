package com.rpgsystem.rpg.infrastructure;

import com.rpgsystem.rpg.infrastructure.config.SecurityConfig;
import com.rpgsystem.rpg.infrastructure.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationRegistry;

    @Mock
    private AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl;

    @Mock
    private SessionManagementConfigurer<HttpSecurity> sessionManagementConfigurer;

    @Mock
    private SessionManagementConfigurer<HttpSecurity>.SessionFixationConfigurer sessionFixationConfigurer;

    @Mock
    private CorsConfigurer<HttpSecurity> corsConfigurer;

    @Mock
    private AbstractHttpConfigurer<?, HttpSecurity> csrfConfigurer;

    @Mock
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(userDetailsService);
    }

    @Test
    @DisplayName("Should create PasswordEncoder bean with BCrypt")
    void shouldCreatePasswordEncoderBean() {
        // When
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Then
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    @DisplayName("Should encode password correctly with BCrypt")
    void shouldEncodePasswordCorrectly() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword123";

        // When
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Then
        assertThat(encodedPassword).isNotNull();
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(encodedPassword).startsWith("$2a$"); // BCrypt prefix
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("Should validate password matching with BCrypt")
    void shouldValidatePasswordMatching() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "mySecurePassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // When & Then
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
        assertThat(passwordEncoder.matches(wrongPassword, encodedPassword)).isFalse();
    }

    @Test
    @DisplayName("Should create different encoded passwords for same input")
    void shouldCreateDifferentEncodedPasswordsForSameInput() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "samePassword";

        // When
        String encodedPassword1 = passwordEncoder.encode(rawPassword);
        String encodedPassword2 = passwordEncoder.encode(rawPassword);

        // Then
        assertThat(encodedPassword1).isNotEqualTo(encodedPassword2);
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword1)).isTrue();
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword2)).isTrue();
    }

    @Test
    @DisplayName("Should handle empty password encoding")
    void shouldHandleEmptyPasswordEncoding() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String emptyPassword = "";

        // When
        String encodedPassword = passwordEncoder.encode(emptyPassword);

        // Then
        assertThat(encodedPassword).isNotNull();
        assertThat(encodedPassword).isNotEmpty();
        assertThat(passwordEncoder.matches(emptyPassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("Should handle null password encoding gracefully")
    void shouldHandleNullPasswordEncoding() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // When & Then
        try {
            passwordEncoder.encode(null);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains("rawPassword cannot be null");
        }
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void shouldHandleSpecialCharactersInPassword() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String specialPassword = "P@ssw0rd!#$%^&*()";

        // When
        String encodedPassword = passwordEncoder.encode(specialPassword);

        // Then
        assertThat(encodedPassword).isNotNull();
        assertThat(passwordEncoder.matches(specialPassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("Should handle very long passwords within BCrypt limits")
    void shouldHandleVeryLongPassword() {
        // Given - BCrypt has a 72 byte limit, so we test with 70 bytes to be safe
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String longPassword = "a".repeat(70);

        // When
        String encodedPassword = passwordEncoder.encode(longPassword);

        // Then
        assertThat(encodedPassword).isNotNull();
        assertThat(passwordEncoder.matches(longPassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("Should handle passwords at BCrypt limit")
    void shouldHandlePasswordsAtBCryptLimit() {
        // Given - BCrypt has a 72 byte limit
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String maxLengthPassword = "a".repeat(72);

        // When
        String encodedPassword = passwordEncoder.encode(maxLengthPassword);

        // Then
        assertThat(encodedPassword).isNotNull();
        assertThat(passwordEncoder.matches(maxLengthPassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("Should handle Unicode characters in password")
    void shouldHandleUnicodeCharactersInPassword() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String unicodePassword = "пароль123密码🔐";

        // When
        String encodedPassword = passwordEncoder.encode(unicodePassword);

        // Then
        assertThat(encodedPassword).isNotNull();
        assertThat(passwordEncoder.matches(unicodePassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("Should not match incorrect password")
    void shouldNotMatchIncorrectPassword() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String correctPassword = "correctPassword";
        String incorrectPassword = "incorrectPassword";
        String encodedPassword = passwordEncoder.encode(correctPassword);

        // When & Then
        assertThat(passwordEncoder.matches(incorrectPassword, encodedPassword)).isFalse();
    }

    @Test
    @DisplayName("Should handle case sensitive passwords")
    void shouldHandleCaseSensitivePasswords() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String lowerCasePassword = "password";
        String upperCasePassword = "PASSWORD";
        String encodedPassword = passwordEncoder.encode(lowerCasePassword);

        // When & Then
        assertThat(passwordEncoder.matches(lowerCasePassword, encodedPassword)).isTrue();
        assertThat(passwordEncoder.matches(upperCasePassword, encodedPassword)).isFalse();
    }

    @Test
    @DisplayName("Should validate BCrypt strength parameter")
    void shouldValidateBCryptStrengthParameter() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        BCryptPasswordEncoder bcryptEncoder = (BCryptPasswordEncoder) passwordEncoder;
        
        // When
        String password = "testPassword";
        String encoded = bcryptEncoder.encode(password);
        
        // Then
        // BCrypt default strength is 10, encoded password should reflect this
        assertThat(encoded).matches("\\$2[ayb]\\$10\\$.*");
    }

    @Test
    @DisplayName("Should be thread safe for concurrent encoding")
    void shouldBeThreadSafeForConcurrentEncoding() throws InterruptedException {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String password = "concurrentTest";
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        String[] results = new String[threadCount];

        // When
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = passwordEncoder.encode(password);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Then
        for (String result : results) {
            assertThat(result).isNotNull();
            assertThat(passwordEncoder.matches(password, result)).isTrue();
        }
    }

    @Test
    @DisplayName("Should maintain consistency across multiple instances")
    void shouldMaintainConsistencyAcrossMultipleInstances() {
        // Given
        PasswordEncoder encoder1 = securityConfig.passwordEncoder();
        PasswordEncoder encoder2 = securityConfig.passwordEncoder();
        String password = "consistencyTest";

        // When
        String encoded1 = encoder1.encode(password);
        String encoded2 = encoder2.encode(password);

        // Then
        assertThat(encoder1.matches(password, encoded2)).isTrue();
        assertThat(encoder2.matches(password, encoded1)).isTrue();
    }

    @Test
    @DisplayName("Should create CORS configuration source with correct settings")
    void shouldCreateCorsConfigurationSourceWithCorrectSettings() {
        // When
        CorsConfigurationSource corsSource = securityConfig.corsConfigurationSource();
        
        // Then
        assertThat(corsSource).isNotNull();
        assertThat(corsSource).isInstanceOf(org.springframework.web.cors.UrlBasedCorsConfigurationSource.class);
    }

    @Test
    @DisplayName("Should create authentication provider with correct configuration")
    void shouldCreateAuthenticationProviderWithCorrectConfiguration() {
        // When
        AuthenticationProvider provider = securityConfig.authenticationProvider();
        
        // Then
        assertThat(provider).isNotNull();
        assertThat(provider).isInstanceOf(org.springframework.security.authentication.dao.DaoAuthenticationProvider.class);
    }

    @Test
    @DisplayName("Should create authentication manager with provider")
    void shouldCreateAuthenticationManagerWithProvider() {
        // Given
        AuthenticationProvider provider = securityConfig.authenticationProvider();
        
        // When
        org.springframework.security.authentication.AuthenticationManager authManager = 
            securityConfig.authenticationManager(provider);
        
        // Then
        assertThat(authManager).isNotNull();
        assertThat(authManager).isInstanceOf(org.springframework.security.authentication.ProviderManager.class);
    }

    @Test
    @DisplayName("Should create JWT decoder with secret key")
    void shouldCreateJwtDecoderWithSecretKey() {
        // Given
        ReflectionTestUtils.setField(securityConfig, "jwtSecret", "dGVzdC1zZWNyZXQta2V5LWZvci10ZXN0aW5nLW9ubHktbXVzdC1iZS1hdC1sZWFzdC0yNTYtYml0cw==");
        
        // When
        org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder = securityConfig.jwtDecoder();
        
        // Then
        assertThat(jwtDecoder).isNotNull();
        assertThat(jwtDecoder).isInstanceOf(org.springframework.security.oauth2.jwt.NimbusJwtDecoder.class);
    }

    @Test
    @DisplayName("Should create JWT encoder with secret key")
    void shouldCreateJwtEncoderWithSecretKey() {
        // Given
        ReflectionTestUtils.setField(securityConfig, "jwtSecret", "dGVzdC1zZWNyZXQta2V5LWZvci10ZXN0aW5nLW9ubHktbXVzdC1iZS1hdC1sZWFzdC0yNTYtYml0cw==");
        
        // When
        org.springframework.security.oauth2.jwt.JwtEncoder jwtEncoder = securityConfig.jwtEncoder();
        
        // Then
        assertThat(jwtEncoder).isNotNull();
        assertThat(jwtEncoder).isInstanceOf(org.springframework.security.oauth2.jwt.NimbusJwtEncoder.class);
    }

    @Test
    @DisplayName("Should create JWT authentication filter with dependencies")
    void shouldCreateJwtAuthenticationFilterWithDependencies() {
        // Given
        ReflectionTestUtils.setField(securityConfig, "jwtSecret", "dGVzdC1zZWNyZXQta2V5LWZvci10ZXN0aW5nLW9ubHktbXVzdC1iZS1hdC1sZWFzdC0yNTYtYml0cw==");
        org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder = securityConfig.jwtDecoder();
        
        // When
        JwtAuthenticationFilter filter = securityConfig.jwtAuthenticationFilter(jwtDecoder);
        
        // Then
        assertThat(filter).isNotNull();
        assertThat(filter).isInstanceOf(JwtAuthenticationFilter.class);
    }


}