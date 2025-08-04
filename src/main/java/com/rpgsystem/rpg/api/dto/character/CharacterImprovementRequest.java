package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterImprovementRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Cost is required")
    private Integer cost;

    @NotBlank(message = "Kit value is required")
    private Integer kitValue;

}
