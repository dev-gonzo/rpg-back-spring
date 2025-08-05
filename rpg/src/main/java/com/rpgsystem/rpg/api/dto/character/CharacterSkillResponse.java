package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CharacterSkillResponse {

    private String id;
    private String characterId;

    private String skill;
    private String group;
    private String attribute;

    private Integer cost;
    private Integer kitValue;

    private Instant createdAt;
    private Instant updatedAt;
}
