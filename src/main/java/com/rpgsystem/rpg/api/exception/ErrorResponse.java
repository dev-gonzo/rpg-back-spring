package com.rpgsystem.rpg.api.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
public class ErrorResponse {

    private final String title;
    private final String detail;
    private final Instant timestamp;
    private final List<ValidationError> errors;

    private ErrorResponse(String title, String detail, List<ValidationError> errors) {
        this.title = title;
        this.detail = detail;
        this.timestamp = Instant.now();
        this.errors = errors;
    }

    public static ErrorResponse of(String title, String detail) {
        return new ErrorResponse(title, detail, null);
    }

    public static ErrorResponse of(String title, String detail, List<ValidationError> errors) {
        return new ErrorResponse(title, detail, errors);
    }
}
