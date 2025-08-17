package com.rpgsystem.rpg.api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    @DisplayName("Should create ErrorResponse with title and detail only")
    void shouldCreateErrorResponseWithTitleAndDetailOnly() {
        // Given
        String title = "Validation Error";
        String detail = "Invalid input provided";
        Instant beforeCreation = Instant.now();

        // When
        ErrorResponse errorResponse = ErrorResponse.of(title, detail);
        Instant afterCreation = Instant.now();

        // Then
        assertThat(errorResponse.getTitle()).isEqualTo(title);
        assertThat(errorResponse.getDetail()).isEqualTo(detail);
        assertThat(errorResponse.getTimestamp()).isBetween(beforeCreation, afterCreation);
        assertThat(errorResponse.getErrors()).isNull();
    }

    @Test
    @DisplayName("Should create ErrorResponse with title, detail and validation errors")
    void shouldCreateErrorResponseWithTitleDetailAndValidationErrors() {
        // Given
        String title = "Validation Failed";
        String detail = "Multiple field validation errors";
        List<ValidationError> validationErrors = Arrays.asList(
                ValidationError.of("name", "Name is required"),
                ValidationError.of("email", "Email format is invalid")
        );
        Instant beforeCreation = Instant.now();

        // When
        ErrorResponse errorResponse = ErrorResponse.of(title, detail, validationErrors);
        Instant afterCreation = Instant.now();

        // Then
        assertThat(errorResponse.getTitle()).isEqualTo(title);
        assertThat(errorResponse.getDetail()).isEqualTo(detail);
        assertThat(errorResponse.getTimestamp()).isBetween(beforeCreation, afterCreation);
        assertThat(errorResponse.getErrors()).isNotNull();
        assertThat(errorResponse.getErrors()).hasSize(2);
        assertThat(errorResponse.getErrors().get(0).getField()).isEqualTo("name");
        assertThat(errorResponse.getErrors().get(0).getMessage()).isEqualTo("Name is required");
        assertThat(errorResponse.getErrors().get(1).getField()).isEqualTo("email");
        assertThat(errorResponse.getErrors().get(1).getMessage()).isEqualTo("Email format is invalid");
    }

    @Test
    @DisplayName("Should create ErrorResponse with empty validation errors list")
    void shouldCreateErrorResponseWithEmptyValidationErrorsList() {
        // Given
        String title = "Validation Error";
        String detail = "No specific field errors";
        List<ValidationError> emptyErrors = Arrays.asList();

        // When
        ErrorResponse errorResponse = ErrorResponse.of(title, detail, emptyErrors);

        // Then
        assertThat(errorResponse.getTitle()).isEqualTo(title);
        assertThat(errorResponse.getDetail()).isEqualTo(detail);
        assertThat(errorResponse.getTimestamp()).isNotNull();
        assertThat(errorResponse.getErrors()).isNotNull();
        assertThat(errorResponse.getErrors()).isEmpty();
    }

    @Test
    @DisplayName("Should create ErrorResponse with null validation errors")
    void shouldCreateErrorResponseWithNullValidationErrors() {
        // Given
        String title = "Server Error";
        String detail = "Internal server error occurred";
        List<ValidationError> nullErrors = null;

        // When
        ErrorResponse errorResponse = ErrorResponse.of(title, detail, nullErrors);

        // Then
        assertThat(errorResponse.getTitle()).isEqualTo(title);
        assertThat(errorResponse.getDetail()).isEqualTo(detail);
        assertThat(errorResponse.getTimestamp()).isNotNull();
        assertThat(errorResponse.getErrors()).isNull();
    }

    @Test
    @DisplayName("Should handle null title and detail")
    void shouldHandleNullTitleAndDetail() {
        // Given
        String nullTitle = null;
        String nullDetail = null;

        // When
        ErrorResponse errorResponse = ErrorResponse.of(nullTitle, nullDetail);

        // Then
        assertThat(errorResponse.getTitle()).isNull();
        assertThat(errorResponse.getDetail()).isNull();
        assertThat(errorResponse.getTimestamp()).isNotNull();
        assertThat(errorResponse.getErrors()).isNull();
    }

    @Test
    @DisplayName("Should handle empty title and detail")
    void shouldHandleEmptyTitleAndDetail() {
        // Given
        String emptyTitle = "";
        String emptyDetail = "";

        // When
        ErrorResponse errorResponse = ErrorResponse.of(emptyTitle, emptyDetail);

        // Then
        assertThat(errorResponse.getTitle()).isEmpty();
        assertThat(errorResponse.getDetail()).isEmpty();
        assertThat(errorResponse.getTimestamp()).isNotNull();
        assertThat(errorResponse.getErrors()).isNull();
    }

    @Test
    @DisplayName("Should create ErrorResponse with single validation error")
    void shouldCreateErrorResponseWithSingleValidationError() {
        // Given
        String title = "Field Validation Error";
        String detail = "Single field validation failed";
        List<ValidationError> singleError = Arrays.asList(
                ValidationError.of("password", "Password must be at least 8 characters")
        );

        // When
        ErrorResponse errorResponse = ErrorResponse.of(title, detail, singleError);

        // Then
        assertThat(errorResponse.getTitle()).isEqualTo(title);
        assertThat(errorResponse.getDetail()).isEqualTo(detail);
        assertThat(errorResponse.getTimestamp()).isNotNull();
        assertThat(errorResponse.getErrors()).hasSize(1);
        assertThat(errorResponse.getErrors().get(0).getField()).isEqualTo("password");
        assertThat(errorResponse.getErrors().get(0).getMessage()).isEqualTo("Password must be at least 8 characters");
    }

    @Test
    @DisplayName("Should ensure timestamp is always set to current time")
    void shouldEnsureTimestampIsAlwaysSetToCurrentTime() {
        // Given
        Instant before = Instant.now();
        
        // When
        ErrorResponse errorResponse1 = ErrorResponse.of("Error 1", "Detail 1");
        
        // Small delay to ensure different timestamps
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        ErrorResponse errorResponse2 = ErrorResponse.of("Error 2", "Detail 2");
        Instant after = Instant.now();

        // Then
        assertThat(errorResponse1.getTimestamp()).isBetween(before, after);
        assertThat(errorResponse2.getTimestamp()).isBetween(before, after);
        assertThat(errorResponse2.getTimestamp()).isAfterOrEqualTo(errorResponse1.getTimestamp());
    }
}