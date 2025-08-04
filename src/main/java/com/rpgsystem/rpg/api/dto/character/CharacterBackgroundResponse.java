package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CharacterBackgroundResponse {

    private String id;
    private String characterId;

    private String title;
    private String text;
    private boolean isPublic;

    private Instant createdAt;
    private Instant updatedAt;
}
