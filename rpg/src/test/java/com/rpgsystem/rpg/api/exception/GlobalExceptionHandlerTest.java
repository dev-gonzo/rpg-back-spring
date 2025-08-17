package com.rpgsystem.rpg.api.exception;

import com.fasterxml.jackson.core.exc.InputCoercionException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.rpgsystem.rpg.domain.exception.DomainException;
import com.rpgsystem.rpg.domain.exception.ImageProcessingException;
import com.rpgsystem.rpg.domain.exception.InvalidCredentialsException;
import com.rpgsystem.rpg.domain.exception.UnauthorizedActionException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        // Setup comum para os testes
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException and return BAD_REQUEST")
    void shouldHandleIllegalArgumentException() {
        // Given
        String errorMessage = "Invalid argument provided";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgument(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Invalid Argument");
        assertThat(response.getBody().getDetail()).isEqualTo(errorMessage);
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("Should handle EntityNotFoundException and return NOT_FOUND")
    void shouldHandleEntityNotFoundException() {
        // Given
        String errorMessage = "Entity with id 123 not found";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNotFound(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Resource Not Found");
        assertThat(response.getBody().getDetail()).isEqualTo(errorMessage);
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException with InvalidFormatException cause")
    void shouldHandleHttpMessageNotReadableExceptionWithInvalidFormatException() {
        // Given
        InvalidFormatException invalidFormatException = mock(InvalidFormatException.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid JSON", invalidFormatException);
        
        when(invalidFormatException.getPath()).thenReturn(List.of());
        when(invalidFormatException.getTargetType()).thenReturn((Class) Integer.class);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleJsonParse(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getDetail()).isEqualTo("Invalid fields found");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("unknown");
        assertThat(response.getBody().getErrors().get(0).getMessage()).contains("Expected type 'Integer'");
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException with InputCoercionException cause")
    void shouldHandleHttpMessageNotReadableExceptionWithInputCoercionException() {
        // Given
        InputCoercionException inputCoercionException = mock(InputCoercionException.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid JSON", inputCoercionException);
        
        when(inputCoercionException.getOriginalMessage()).thenReturn("Number overflow");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleJsonParse(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getDetail()).isEqualTo("Invalid fields found");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("unknown");
        assertThat(response.getBody().getErrors().get(0).getMessage()).contains("Invalid number format or overflow");
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException with generic cause")
    void shouldHandleHttpMessageNotReadableExceptionWithGenericCause() {
        // Given
        RuntimeException genericException = new RuntimeException("Generic error");
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid JSON", genericException);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleJsonParse(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("JSON Error");
        assertThat(response.getBody().getDetail()).isEqualTo("Malformed request payload.");
        assertThat(response.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException and return validation errors")
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        FieldError fieldError1 = new FieldError("user", "name", "Name is required");
        FieldError fieldError2 = new FieldError("user", "email", "Email is invalid");
        
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(methodArgumentNotValidException);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getDetail()).isEqualTo("Invalid fields found");
        assertThat(response.getBody().getErrors()).hasSize(2);
        assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("name");
        assertThat(response.getBody().getErrors().get(0).getMessage()).isEqualTo("Name is required");
        assertThat(response.getBody().getErrors().get(1).getField()).isEqualTo("email");
        assertThat(response.getBody().getErrors().get(1).getMessage()).isEqualTo("Email is invalid");
    }

    @Test
    @DisplayName("Should handle InvalidCredentialsException and return UNAUTHORIZED")
    void shouldHandleInvalidCredentialsException() {
        // Given
        InvalidCredentialsException exception = new InvalidCredentialsException();

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidCredentials(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Authentication Failed");
        assertThat(response.getBody().getDetail()).isEqualTo("Invalid email or password.");
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("Should handle UnauthorizedActionException and return FORBIDDEN")
    void shouldHandleUnauthorizedActionException() {
        // Given
        String errorMessage = "User does not have permission to access this resource";
        UnauthorizedActionException exception = new UnauthorizedActionException(errorMessage);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUnauthorized(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Unauthorized");
        assertThat(response.getBody().getDetail()).isEqualTo(errorMessage);
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("Should handle DomainException and return BAD_REQUEST")
    void shouldHandleDomainException() {
        // Given
        String errorMessage = "Business rule violation";
        DomainException exception = new DomainException(errorMessage);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDomainException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Domain Error");
        assertThat(response.getBody().getDetail()).isEqualTo(errorMessage);
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("Should handle ImageProcessingException and return BAD_REQUEST")
    void shouldHandleImageProcessingException() {
        // Given
        String errorMessage = "Failed to process image";
        RuntimeException cause = new RuntimeException("IO Error");
        ImageProcessingException exception = new ImageProcessingException(errorMessage, cause);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleImageProcessing(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Image Error");
        assertThat(response.getBody().getDetail()).isEqualTo(errorMessage);
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("Should handle generic Exception and return INTERNAL_SERVER_ERROR")
    void shouldHandleGenericException() {
        // Given
        Exception exception = new RuntimeException("Unexpected error");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGeneric(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getDetail()).isEqualTo("An unexpected error occurred.");
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException without cause")
    void shouldHandleHttpMessageNotReadableExceptionWithoutCause() {
        // Given
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid JSON");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleJsonParse(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("JSON Error");
        assertThat(response.getBody().getDetail()).isEqualTo("Malformed request payload.");
        assertThat(response.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with empty field errors")
    void shouldHandleMethodArgumentNotValidExceptionWithEmptyErrors() {
        // Given
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(methodArgumentNotValidException);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getDetail()).isEqualTo("Invalid fields found");
        assertThat(response.getBody().getErrors()).isEmpty();
    }

    @Test
    @DisplayName("Should create ErrorResponse with correct timestamp")
    void shouldCreateErrorResponseWithCorrectTimestamp() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Test error");
        java.time.Instant beforeCall = java.time.Instant.now();

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgument(exception);
        java.time.Instant afterCall = java.time.Instant.now();

        // Then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTimestamp()).isBetween(beforeCall, afterCall);
    }

    @Test
    @DisplayName("Should handle InvalidFormatException with non-empty path")
    void shouldHandleInvalidFormatExceptionWithNonEmptyPath() {
        // Given
        InvalidFormatException invalidFormatException = mock(InvalidFormatException.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid JSON", invalidFormatException);
        
        // Mock path with field name
        com.fasterxml.jackson.core.JsonParser.NumberType numberType = mock(com.fasterxml.jackson.core.JsonParser.NumberType.class);
        com.fasterxml.jackson.databind.JsonMappingException.Reference reference = 
            new com.fasterxml.jackson.databind.JsonMappingException.Reference(null, "testField");
        
        when(invalidFormatException.getPath()).thenReturn(List.of(reference));
        when(invalidFormatException.getTargetType()).thenReturn((Class) String.class);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleJsonParse(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("testField");
    }

    @Test
    @DisplayName("Should handle InvalidFormatException with null target type")
    void shouldHandleInvalidFormatExceptionWithNullTargetType() {
        // Given
        InvalidFormatException invalidFormatException = mock(InvalidFormatException.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid JSON", invalidFormatException);
        
        when(invalidFormatException.getPath()).thenReturn(List.of());
        when(invalidFormatException.getTargetType()).thenReturn(null);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleJsonParse(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).getMessage()).contains("Expected type 'unknown'");
    }

    @Test
    @DisplayName("Should handle InputCoercionException with message containing 'from String value'")
    void shouldHandleInputCoercionExceptionWithStringValue() {
        // Given
        InputCoercionException inputCoercionException = mock(InputCoercionException.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
            "Cannot coerce String value \"invalid\" to Integer", inputCoercionException);
        
        when(inputCoercionException.getOriginalMessage()).thenReturn("Number overflow");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleJsonParse(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).getMessage()).contains("Invalid number format or overflow: Number overflow");
    }

    @Test
    @DisplayName("Should handle InputCoercionException with null message")
    void shouldHandleInputCoercionExceptionWithNullMessage() {
        // Given
        InputCoercionException inputCoercionException = mock(InputCoercionException.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(null, inputCoercionException);
        
        when(inputCoercionException.getOriginalMessage()).thenReturn("Coercion error");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleJsonParse(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("unknown");
    }

    @Test
    @DisplayName("Should handle InputCoercionException with message containing 'from String value'")
    void shouldHandleInputCoercionExceptionWithFromStringValueMessage() {
        // Given
        InputCoercionException inputCoercionException = mock(InputCoercionException.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
            "JSON parse error: Cannot deserialize value from String value \"invalid\" for type Integer", 
            inputCoercionException);
        
        when(inputCoercionException.getOriginalMessage()).thenReturn("Cannot coerce String value");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleJsonParse(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("unknown");
        assertThat(response.getBody().getErrors().get(0).getMessage()).contains("Invalid number format or overflow: Cannot coerce String value");
    }
}