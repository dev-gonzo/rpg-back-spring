package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CharacterRitualPowerResponse {

    private String id;
    private String characterId;

    private String name;
    private String pathsForms;
    private String description;
    private String bookPage;

    private Instant createdAt;
    private Instant updatedAt;
}
