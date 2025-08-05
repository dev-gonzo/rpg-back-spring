package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterAttributeModRequest {

    @NotNull(message = "Character ID must not be null")
    private String characterId;


    private Integer conMod;
    private Integer frMod;
    private Integer dexMod;
    private Integer agiMod;
    private Integer intMod;
    private Integer willMod;
    private Integer perMod;
    private Integer carMod;
}
