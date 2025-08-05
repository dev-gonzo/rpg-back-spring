package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CharacterCombatSkillResponse {

    private String id;
    private String characterId;

    private String skill;
    private String group;
    private String attribute;

    private Integer attackCost;
    private Integer defenseCost;

    private Integer attackKitValue;
    private Integer defenseKitValue;

    private Instant createdAt;
    private Instant updatedAt;
}
