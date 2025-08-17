package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.UserSimpleResponse;
import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserSimpleDtoBuilder Tests")
class UserSimpleDtoBuilderTest {

    @Test
    @DisplayName("Should build UserSimpleResponse from User with all fields")
    void shouldBuildUserSimpleResponseFromUserWithAllFields() {
        // Given
        User user = User.builder()
                .id("user-123")
                .name("Test User")
                .email("test@example.com")
                .password("encodedPassword")
                .country("Brasil")
                .city("São Paulo")
                .isMaster(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When
        UserSimpleResponse result = UserSimpleDtoBuilder.build(user);

        // Then
        assertNotNull(result);
        assertEquals("user-123", result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    @DisplayName("Should build UserSimpleResponse from User with null values")
    void shouldBuildUserSimpleResponseFromUserWithNullValues() {
        // Given
        User user = User.builder()
                .id(null)
                .name(null)
                .email(null)
                .password("password")
                .country("Country")
                .city("City")
                .isMaster(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When
        UserSimpleResponse result = UserSimpleDtoBuilder.build(user);

        // Then
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getName());
        assertNull(result.getEmail());
    }

    @Test
    @DisplayName("Should build UserSimpleResponse from User with empty strings")
    void shouldBuildUserSimpleResponseFromUserWithEmptyStrings() {
        // Given
        User user = User.builder()
                .id("")
                .name("")
                .email("")
                .password("password")
                .country("Country")
                .city("City")
                .isMaster(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When
        UserSimpleResponse result = UserSimpleDtoBuilder.build(user);

        // Then
        assertNotNull(result);
        assertEquals("", result.getId());
        assertEquals("", result.getName());
        assertEquals("", result.getEmail());
    }

    @Test
    @DisplayName("Should build UserSimpleResponse from User with long values")
    void shouldBuildUserSimpleResponseFromUserWithLongValues() {
        // Given
        String longId = "a".repeat(1000);
        String longName = "Very Long User Name ".repeat(50);
        String longEmail = "very.long.email.address." + "test".repeat(100) + "@example.com";
        
        User user = User.builder()
                .id(longId)
                .name(longName)
                .email(longEmail)
                .password("password")
                .country("Country")
                .city("City")
                .isMaster(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When
        UserSimpleResponse result = UserSimpleDtoBuilder.build(user);

        // Then
        assertNotNull(result);
        assertEquals(longId, result.getId());
        assertEquals(longName, result.getName());
        assertEquals(longEmail, result.getEmail());
        assertTrue(result.getId().length() == 1000);
        assertTrue(result.getName().length() > 500);
        assertTrue(result.getEmail().contains("test".repeat(100)));
    }

    @Test
    @DisplayName("Should build UserSimpleResponse from User with Unicode characters")
    void shouldBuildUserSimpleResponseFromUserWithUnicodeCharacters() {
        // Given
        User user = User.builder()
                .id("用户-123-🆔")
                .name("Usuário Élfico 魔法師 🧙‍♂️")
                .email("usuário.élfico@domínio.com.br")
                .password("password")
                .country("Brasil")
                .city("São Paulo")
                .isMaster(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When
        UserSimpleResponse result = UserSimpleDtoBuilder.build(user);

        // Then
        assertNotNull(result);
        assertEquals("用户-123-🆔", result.getId());
        assertEquals("Usuário Élfico 魔法師 🧙‍♂️", result.getName());
        assertEquals("usuário.élfico@domínio.com.br", result.getEmail());
        assertTrue(result.getId().contains("用户"));
        assertTrue(result.getId().contains("🆔"));
        assertTrue(result.getName().contains("魔法師"));
        assertTrue(result.getName().contains("🧙‍♂️"));
        assertTrue(result.getEmail().contains("usuário"));
        assertTrue(result.getEmail().contains("élfico"));
        assertTrue(result.getEmail().contains("domínio"));
    }

    @Test
    @DisplayName("Should build UserSimpleResponse from User with special characters")
    void shouldBuildUserSimpleResponseFromUserWithSpecialCharacters() {
        // Given
        User user = User.builder()
                .id("user@#$%^&*()_+-=[]{}|;':,.<>?/~`")
                .name("User with Special !@#$%^&*() Characters")
                .email("special+chars@test-domain.co.uk")
                .password("password")
                .country("Country")
                .city("City")
                .isMaster(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When
        UserSimpleResponse result = UserSimpleDtoBuilder.build(user);

        // Then
        assertNotNull(result);
        assertEquals("user@#$%^&*()_+-=[]{}|;':,.<>?/~`", result.getId());
        assertEquals("User with Special !@#$%^&*() Characters", result.getName());
        assertEquals("special+chars@test-domain.co.uk", result.getEmail());
        assertTrue(result.getId().contains("@#$%^&*()"));
        assertTrue(result.getName().contains("!@#$%^&*()"));
        assertTrue(result.getEmail().contains("+"));
        assertTrue(result.getEmail().contains("-"));
    }

    @Test
    @DisplayName("Should return null when User is null")
    void shouldReturnNullWhenUserIsNull() {
        // Given
        User nullUser = null;

        // When
        UserSimpleResponse result = UserSimpleDtoBuilder.build(nullUser);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("Should build UserSimpleResponse from User with minimal required fields")
    void shouldBuildUserSimpleResponseFromUserWithMinimalRequiredFields() {
        // Given
        User user = User.builder()
                .id("min-user")
                .name("Min")
                .email("m@e.co")
                .password("p")
                .country("C")
                .city("C")
                .isMaster(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When
        UserSimpleResponse result = UserSimpleDtoBuilder.build(user);

        // Then
        assertNotNull(result);
        assertEquals("min-user", result.getId());
        assertEquals("Min", result.getName());
        assertEquals("m@e.co", result.getEmail());
    }

    @Test
    @DisplayName("Should build UserSimpleResponse from User with whitespace values")
    void shouldBuildUserSimpleResponseFromUserWithWhitespaceValues() {
        // Given
        User user = User.builder()
                .id("   user-with-spaces   ")
                .name("  User Name  ")
                .email("  email@domain.com  ")
                .password("password")
                .country("Country")
                .city("City")
                .isMaster(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When
        UserSimpleResponse result = UserSimpleDtoBuilder.build(user);

        // Then
        assertNotNull(result);
        assertEquals("   user-with-spaces   ", result.getId());
        assertEquals("  User Name  ", result.getName());
        assertEquals("  email@domain.com  ", result.getEmail());
        assertTrue(result.getId().startsWith("   "));
        assertTrue(result.getId().endsWith("   "));
        assertTrue(result.getName().startsWith("  "));
        assertTrue(result.getName().endsWith("  "));
        assertTrue(result.getEmail().startsWith("  "));
        assertTrue(result.getEmail().endsWith("  "));
    }

    @Test
    @DisplayName("Should not allow instantiation of utility class")
    void shouldNotAllowInstantiationOfUtilityClass() throws NoSuchMethodException {
        // Given
        Constructor<UserSimpleDtoBuilder> constructor = UserSimpleDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}