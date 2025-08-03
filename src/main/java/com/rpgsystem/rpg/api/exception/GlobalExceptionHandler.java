package com.rpgsystem.rpg.api.exception;

import com.fasterxml.jackson.core.exc.InputCoercionException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.rpgsystem.rpg.domain.exception.DomainException;
import com.rpgsystem.rpg.domain.exception.InvalidCredentialsException;
import com.rpgsystem.rpg.domain.exception.UnauthorizedActionException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of("Invalid Argument", ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("Resource Not Found", ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParse(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife) {
            String field = ife.getPath().isEmpty() ? "unknown" : ife.getPath().get(0).getFieldName();
            String expectedType = ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : "unknown";

            ValidationError validationError = ValidationError.of(
                    field,
                    String.format("Expected type '%s'", expectedType)
            );

            return ResponseEntity.badRequest().body(
                    ErrorResponse.of("Validation Failed", "Invalid fields found", List.of(validationError))
            );
        }

        if (cause instanceof InputCoercionException ice) {
            String message = ice.getOriginalMessage();


            String field = "unknown";
            String rawMsg = ex.getMessage();
            if (rawMsg != null && rawMsg.contains("from String value")) {
                int start = rawMsg.indexOf("value") + 6;
                field = "unknown";
            }

            ValidationError validationError = ValidationError.of(
                    field,
                    "Invalid number format or overflow: " + message
            );

            return ResponseEntity.badRequest().body(
                    ErrorResponse.of("Validation Failed", "Invalid fields found", List.of(validationError))
            );
        }

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of("JSON Error", "Malformed request payload."));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ValidationError.of(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of("Validation Failed", "Invalid fields found", errors));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of("Authentication Failed", ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedActionException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of("Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of("Domain Error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("Internal Server Error", "An unexpected error occurred."));
    }
}
