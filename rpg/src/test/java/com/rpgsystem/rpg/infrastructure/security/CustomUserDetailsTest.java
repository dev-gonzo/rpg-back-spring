package com.rpgsystem.rpg.infrastructure.security;

import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CustomUserDetails Tests")
class CustomUserDetailsTest {

    private User mockUser;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId("1");
        mockUser.setName("Test User");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword123");
        
        customUserDetails = new CustomUserDetails(mockUser);
    }

    @Test
    @DisplayName("Should return correct authorities")
    void shouldReturnCorrectAuthorities() {
        // When
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();

        // Then
        assertThat(authorities).isNotNull();
        assertThat(authorities).isEmpty();
    }

    @Test
    @DisplayName("Should return user password")
    void shouldReturnUserPassword() {
        // When
        String password = customUserDetails.getPassword();

        // Then
        assertThat(password).isNotNull();
        assertThat(password).isEqualTo("encodedPassword123");
    }

    @Test
    @DisplayName("Should return user email as username")
    void shouldReturnUserEmailAsUsername() {
        // When
        String username = customUserDetails.getUsername();

        // Then
        assertThat(username).isNotNull();
        assertThat(username).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should return true for isAccountNonExpired")
    void shouldReturnTrueForIsAccountNonExpired() {
        // When
        boolean isAccountNonExpired = customUserDetails.isAccountNonExpired();

        // Then
        assertThat(isAccountNonExpired).isTrue();
    }

    @Test
    @DisplayName("Should return true for isAccountNonLocked")
    void shouldReturnTrueForIsAccountNonLocked() {
        // When
        boolean isAccountNonLocked = customUserDetails.isAccountNonLocked();

        // Then
        assertThat(isAccountNonLocked).isTrue();
    }

    @Test
    @DisplayName("Should return true for isCredentialsNonExpired")
    void shouldReturnTrueForIsCredentialsNonExpired() {
        // When
        boolean isCredentialsNonExpired = customUserDetails.isCredentialsNonExpired();

        // Then
        assertThat(isCredentialsNonExpired).isTrue();
    }

    @Test
    @DisplayName("Should return true for isEnabled")
    void shouldReturnTrueForIsEnabled() {
        // When
        boolean isEnabled = customUserDetails.isEnabled();

        // Then
        assertThat(isEnabled).isTrue();
    }

    @Test
    @DisplayName("Should return the original user object")
    void shouldReturnTheOriginalUserObject() {
        // When
        User user = customUserDetails.getUser();

        // Then
        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(mockUser);
        assertThat(user.getId()).isEqualTo("1");
        assertThat(user.getName()).isEqualTo("Test User");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("encodedPassword123");
    }

    @Test
    @DisplayName("Should handle user with null password")
    void shouldHandleUserWithNullPassword() {
        // Given
        User userWithNullPassword = new User();
        userWithNullPassword.setId("2");
        userWithNullPassword.setEmail("null-password@example.com");
        userWithNullPassword.setPassword(null);
        
        CustomUserDetails customUserDetailsWithNullPassword = new CustomUserDetails(userWithNullPassword);

        // When
        String password = customUserDetailsWithNullPassword.getPassword();

        // Then
        assertThat(password).isNull();
    }

    @Test
    @DisplayName("Should handle user with null email")
    void shouldHandleUserWithNullEmail() {
        // Given
        User userWithNullEmail = new User();
        userWithNullEmail.setId("3");
        userWithNullEmail.setEmail(null);
        userWithNullEmail.setPassword("password123");
        
        CustomUserDetails customUserDetailsWithNullEmail = new CustomUserDetails(userWithNullEmail);

        // When
        String username = customUserDetailsWithNullEmail.getUsername();

        // Then
        assertThat(username).isNull();
    }

    @Test
    @DisplayName("Should handle user with empty email")
    void shouldHandleUserWithEmptyEmail() {
        // Given
        User userWithEmptyEmail = new User();
        userWithEmptyEmail.setId("4");
        userWithEmptyEmail.setEmail("");
        userWithEmptyEmail.setPassword("password123");
        
        CustomUserDetails customUserDetailsWithEmptyEmail = new CustomUserDetails(userWithEmptyEmail);

        // When
        String username = customUserDetailsWithEmptyEmail.getUsername();

        // Then
        assertThat(username).isEmpty();
    }

    @Test
    @DisplayName("Should handle user with different email formats")
    void shouldHandleUserWithDifferentEmailFormats() {
        // Given
        User userWithComplexEmail = new User();
        userWithComplexEmail.setId("5");
        userWithComplexEmail.setEmail("user.name+tag@example-domain.co.uk");
        userWithComplexEmail.setPassword("password123");
        
        CustomUserDetails customUserDetailsWithComplexEmail = new CustomUserDetails(userWithComplexEmail);

        // When
        String username = customUserDetailsWithComplexEmail.getUsername();

        // Then
        assertThat(username).isEqualTo("user.name+tag@example-domain.co.uk");
    }

    @Test
    @DisplayName("Should maintain immutability of authorities collection")
    void shouldMaintainImmutabilityOfAuthoritiesCollection() {
        // When
        Collection<? extends GrantedAuthority> authorities1 = customUserDetails.getAuthorities();
        Collection<? extends GrantedAuthority> authorities2 = customUserDetails.getAuthorities();

        // Then
        assertThat(authorities1).isEqualTo(authorities2);
        assertThat(authorities1).isSameAs(authorities2); // Same reference - immutable
    }

    @Test
    @DisplayName("Should create CustomUserDetails with different users")
    void shouldCreateCustomUserDetailsWithDifferentUsers() {
        // Given
        User anotherUser = new User();
        anotherUser.setId("10");
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword("anotherPassword");
        
        CustomUserDetails anotherCustomUserDetails = new CustomUserDetails(anotherUser);

        // When & Then
        assertThat(anotherCustomUserDetails.getUser()).isEqualTo(anotherUser);
        assertThat(anotherCustomUserDetails.getUsername()).isEqualTo("another@example.com");
        assertThat(anotherCustomUserDetails.getPassword()).isEqualTo("anotherPassword");
        assertThat(anotherCustomUserDetails.getUser().getId()).isEqualTo("10");
        
        // Should be different from original
        assertThat(anotherCustomUserDetails.getUser()).isNotEqualTo(customUserDetails.getUser());
        assertThat(anotherCustomUserDetails.getUsername()).isNotEqualTo(customUserDetails.getUsername());
    }
}