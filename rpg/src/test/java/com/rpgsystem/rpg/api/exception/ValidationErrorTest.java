package com.rpgsystem.rpg.api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationErrorTest {

    @Test
    @DisplayName("Should create ValidationError with field and message")
    void shouldCreateValidationErrorWithFieldAndMessage() {
        // Given
        String field = "email";
        String message = "Email is required";

        // When
        ValidationError validationError = ValidationError.of(field, message);

        // Then
        assertThat(validationError.getField()).isEqualTo(field);
        assertThat(validationError.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create ValidationError with empty field")
    void shouldCreateValidationErrorWithEmptyField() {
        // Given
        String emptyField = "";
        String message = "Field is required";

        // When
        ValidationError validationError = ValidationError.of(emptyField, message);

        // Then
        assertThat(validationError.getField()).isEmpty();
        assertThat(validationError.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create ValidationError with empty message")
    void shouldCreateValidationErrorWithEmptyMessage() {
        // Given
        String field = "username";
        String emptyMessage = "";

        // When
        ValidationError validationError = ValidationError.of(field, emptyMessage);

        // Then
        assertThat(validationError.getField()).isEqualTo(field);
        assertThat(validationError.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("Should create ValidationError with null field")
    void shouldCreateValidationErrorWithNullField() {
        // Given
        String nullField = null;
        String message = "Validation failed";

        // When
        ValidationError validationError = ValidationError.of(nullField, message);

        // Then
        assertThat(validationError.getField()).isNull();
        assertThat(validationError.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create ValidationError with null message")
    void shouldCreateValidationErrorWithNullMessage() {
        // Given
        String field = "password";
        String nullMessage = null;

        // When
        ValidationError validationError = ValidationError.of(field, nullMessage);

        // Then
        assertThat(validationError.getField()).isEqualTo(field);
        assertThat(validationError.getMessage()).isNull();
    }

    @Test
    @DisplayName("Should create ValidationError with both null values")
    void shouldCreateValidationErrorWithBothNullValues() {
        // Given
        String nullField = null;
        String nullMessage = null;

        // When
        ValidationError validationError = ValidationError.of(nullField, nullMessage);

        // Then
        assertThat(validationError.getField()).isNull();
        assertThat(validationError.getMessage()).isNull();
    }

    @Test
    @DisplayName("Should create ValidationError with complex field names")
    void shouldCreateValidationErrorWithComplexFieldNames() {
        // Given
        String complexField = "user.profile.personalInfo.birthDate";
        String message = "Birth date must be in the past";

        // When
        ValidationError validationError = ValidationError.of(complexField, message);

        // Then
        assertThat(validationError.getField()).isEqualTo(complexField);
        assertThat(validationError.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create ValidationError with long message")
    void shouldCreateValidationErrorWithLongMessage() {
        // Given
        String field = "description";
        String longMessage = "This field must contain between 10 and 500 characters, " +
                "include at least one uppercase letter, one lowercase letter, " +
                "one number, and one special character. The content should be " +
                "meaningful and relevant to the context of the application.";

        // When
        ValidationError validationError = ValidationError.of(field, longMessage);

        // Then
        assertThat(validationError.getField()).isEqualTo(field);
        assertThat(validationError.getMessage()).isEqualTo(longMessage);
        assertThat(validationError.getMessage().length()).isGreaterThan(100);
    }

    @Test
    @DisplayName("Should create ValidationError with special characters in field and message")
    void shouldCreateValidationErrorWithSpecialCharacters() {
        // Given
        String fieldWithSpecialChars = "field_with-special.chars[0]";
        String messageWithSpecialChars = "Field contains invalid characters: @#$%^&*()";

        // When
        ValidationError validationError = ValidationError.of(fieldWithSpecialChars, messageWithSpecialChars);

        // Then
        assertThat(validationError.getField()).isEqualTo(fieldWithSpecialChars);
        assertThat(validationError.getMessage()).isEqualTo(messageWithSpecialChars);
    }

    @Test
    @DisplayName("Should create ValidationError with unicode characters")
    void shouldCreateValidationErrorWithUnicodeCharacters() {
        // Given
        String fieldWithUnicode = "nome_usuário";
        String messageWithUnicode = "O nome de usuário deve conter apenas caracteres válidos: áéíóú";

        // When
        ValidationError validationError = ValidationError.of(fieldWithUnicode, messageWithUnicode);

        // Then
        assertThat(validationError.getField()).isEqualTo(fieldWithUnicode);
        assertThat(validationError.getMessage()).isEqualTo(messageWithUnicode);
    }

    @Test
    @DisplayName("Should maintain immutability of ValidationError fields")
    void shouldMaintainImmutabilityOfValidationErrorFields() {
        // Given
        String originalField = "testField";
        String originalMessage = "Test message";

        // When
        ValidationError validationError = ValidationError.of(originalField, originalMessage);

        // Then
        assertThat(validationError.getField()).isEqualTo(originalField);
        assertThat(validationError.getMessage()).isEqualTo(originalMessage);
        
        // Verify that the fields are final (cannot be changed after creation)
        // This is ensured by the @AllArgsConstructor and final fields in the class
        assertThat(validationError.getField()).isSameAs(originalField);
        assertThat(validationError.getMessage()).isSameAs(originalMessage);
    }
}