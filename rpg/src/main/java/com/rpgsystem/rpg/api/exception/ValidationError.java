package com.rpgsystem.rpg.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ValidationError {
    private final String field;
    private final String message;
}
