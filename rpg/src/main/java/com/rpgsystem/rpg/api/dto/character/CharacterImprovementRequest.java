package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterImprovementRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Cost is required")
    private Integer cost;

    @NotNull(message = "Kit value is required")
    private Integer kitValue;

}
