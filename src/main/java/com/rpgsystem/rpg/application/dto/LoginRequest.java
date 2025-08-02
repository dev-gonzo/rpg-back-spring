package com.rpgsystem.rpg.application.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "Birth date is required")
        String email,
        @NotNull(message = "Birth date is required")
        String password
) {
}
