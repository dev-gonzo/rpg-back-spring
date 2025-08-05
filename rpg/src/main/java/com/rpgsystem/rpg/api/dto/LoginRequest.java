package com.rpgsystem.rpg.api.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "E-mail is required")
        String email,
        @NotNull(message = "Password is required")
        String password
) {
}
