package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterWeaponRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Damage is required")
    private String damage;

    @NotNull(message = "Initiative is required")
    private Integer initiative;

    private String range;
    private String rof;
    private String ammunition;
    private String bookPage;
}
