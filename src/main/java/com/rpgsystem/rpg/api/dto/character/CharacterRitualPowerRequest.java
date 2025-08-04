package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterRitualPowerRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Paths and forms are required")
    private String pathsForms;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Book page is required")
    private String bookPage;
}
