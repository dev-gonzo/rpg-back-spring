package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record CharacterInfoRequest(
        @NotBlank(message = "Name is required")
        String name,

        String profession,

        @NotNull(message = "Birth date is required")
        LocalDate birthDate,

        String birthPlace,
        String gender,

        @Min(value = 0, message = "Age must be a positive number")
        Integer age,

        @Min(value = 0, message = "Apparent age must be a positive number")
        Integer apparentAge,

        @Positive(message = "Height must be greater than 0")
        Integer heightCm,

        @Positive(message = "Weight must be greater than 0")
        Integer weightKg,

        String religion
) {}
