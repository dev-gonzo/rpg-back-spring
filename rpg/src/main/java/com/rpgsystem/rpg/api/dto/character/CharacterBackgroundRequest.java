package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterBackgroundRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Text is required")
    private String text;

    private boolean isPublic;
}
