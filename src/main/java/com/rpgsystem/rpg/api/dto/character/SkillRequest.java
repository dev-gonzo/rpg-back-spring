package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillRequest {

    @NotBlank(message = "Skill name is required")
    private String skill;

    private String group;

    private String attribute;

    @NotNull(message = "Cost is required")
    private Integer cost;

    @NotNull(message = "Kit value is required")
    private Integer kitValue;
}
