package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterNoteRequest {

    @NotBlank(message = "Note is required")
    private String note;
}
