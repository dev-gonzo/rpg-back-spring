package com.rpgsystem.rpg.infrastructure.security;

import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService Tests")
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("1");
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
    }

    @Test
    @DisplayName("Should load user by email successfully")
    void shouldLoadUserByEmailSuccessfully() {
        // Given
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Should load user by different email successfully")
    void shouldLoadUserByDifferentEmailSuccessfully() {
        // Given
        String email = "different@example.com";
        testUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found by email")
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFoundByEmail() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado com email: " + email);

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException with invalid email format")
    void shouldThrowUsernameNotFoundExceptionWithInvalidEmailFormat() {
        // Given
        String invalidEmail = "invalid-email-format";
        when(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(invalidEmail))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado com email: " + invalidEmail);

        verify(userRepository).findByEmail(invalidEmail);
    }

    @Test
    @DisplayName("Should handle null email gracefully")
    void shouldHandleNullEmailGracefully() {
        // Given
        String nullEmail = null;
        when(userRepository.findByEmail(nullEmail)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(nullEmail))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado com email: null");

        verify(userRepository).findByEmail(nullEmail);
    }

    @Test
    @DisplayName("Should handle empty email gracefully")
    void shouldHandleEmptyEmailGracefully() {
        // Given
        String emptyEmail = "";
        when(userRepository.findByEmail(emptyEmail)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(emptyEmail))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado com email: ");

        verify(userRepository).findByEmail(emptyEmail);
    }

    @Test
    @DisplayName("Should handle whitespace-only email gracefully")
    void shouldHandleWhitespaceOnlyEmailGracefully() {
        // Given
        String whitespaceEmail = "   ";
        when(userRepository.findByEmail(whitespaceEmail)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(whitespaceEmail))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado com email:    ");

        verify(userRepository).findByEmail(whitespaceEmail);
    }

    @Test
    @DisplayName("Should handle email with special characters")
    void shouldHandleEmailWithSpecialCharacters() {
        // Given
        String specialEmail = "test+special.email@example-domain.com";
        testUser.setEmail(specialEmail);
        when(userRepository.findByEmail(specialEmail)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(specialEmail);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        assertThat(userDetails.getUsername()).isEqualTo(specialEmail);
        verify(userRepository).findByEmail(specialEmail);
    }

    @Test
    @DisplayName("Should handle case-sensitive email search")
    void shouldHandleCaseSensitiveEmailSearch() {
        // Given
        String upperCaseEmail = "TEST@EXAMPLE.COM";
        when(userRepository.findByEmail(upperCaseEmail)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(upperCaseEmail))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado com email: " + upperCaseEmail);

        verify(userRepository).findByEmail(upperCaseEmail);
    }

    @Test
    @DisplayName("Should handle long email addresses")
    void shouldHandleLongEmailAddresses() {
        // Given
        String longEmail = "very.long.email.address.with.many.dots@very-long-domain-name-example.com";
        testUser.setEmail(longEmail);
        when(userRepository.findByEmail(longEmail)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(longEmail);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        verify(userRepository).findByEmail(longEmail);
    }

    @Test
    @DisplayName("Should handle repository exception gracefully")
    void shouldHandleRepositoryExceptionGracefully() {
        // Given
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(email))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
        
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Should create User with correct user data")
    void shouldCreateUserWithCorrectUserData() {
        // Given
        String email = "test@example.com";
        testUser.setEmail(email);
        testUser.setPassword("encodedPassword");
        testUser.setMaster(true);
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
        
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Should handle international domain email")
    void shouldHandleInternationalDomainEmail() {
        // Given
        String internationalEmail = "test@münchen.de";
        testUser.setEmail(internationalEmail);
        when(userRepository.findByEmail(internationalEmail)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(internationalEmail);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        verify(userRepository).findByEmail(internationalEmail);
    }

    @Test
    @DisplayName("Should handle Unicode characters in email")
    void shouldHandleUnicodeCharactersInEmail() {
        // Given
        String unicodeEmail = "пользователь@тест.рф";
        testUser.setEmail(unicodeEmail);
        when(userRepository.findByEmail(unicodeEmail)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(unicodeEmail);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        verify(userRepository).findByEmail(unicodeEmail);
    }

    @Test
    @DisplayName("Should verify single repository method call")
    void shouldVerifySingleRepositoryMethodCall() {
        // Given
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When
        customUserDetailsService.loadUserByUsername(email);

        // Then
        verify(userRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }
}