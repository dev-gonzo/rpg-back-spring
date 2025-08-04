package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterCombatSkillRequest {

    @NotBlank(message = "Skill is required")
    private String skill;

    private String group;
    private String attribute;

    @NotNull(message = "Attack cost is required")
    private Integer attackCost;

    @NotNull(message = "Defense cost is required")
    private Integer defenseCost;

    @NotNull(message = "Attack kit value is required")
    private Integer attackKitValue;

    @NotNull(message = "Defense kit value is required")
    private Integer defenseKitValue;
}
