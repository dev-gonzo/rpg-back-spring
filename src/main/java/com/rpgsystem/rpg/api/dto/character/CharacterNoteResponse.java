package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CharacterNoteResponse {

    private String id;
    private String characterId;
    private String note;

    private Instant createdAt;
    private Instant updatedAt;
}
