package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CharacterImprovementResponse {

    private String characterId;
    private String name;
    private Integer cost;
    private Integer kitCost;
    private Instant createdAt;
    private Instant updatedAt;
}
