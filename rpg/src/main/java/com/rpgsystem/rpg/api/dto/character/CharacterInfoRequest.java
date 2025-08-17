package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CharacterInfoRequest {

        @NotBlank(message = "Name is required")
        private String name;

        private String profession;

        private LocalDate birthDate;

        private String birthPlace;
        private String gender;

        @Min(value = 0, message = "Age must be a positive number")
        private Integer age;

        @Min(value = 0, message = "Apparent age must be a positive number")
        private Integer apparentAge;

        @Positive(message = "Height must be greater than 0")
        private Integer heightCm;

        @Positive(message = "Weight must be greater than 0")
        private Integer weightKg;

        private String religion;
}

