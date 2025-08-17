package com.rpgsystem.rpg.api.dto.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterAttributeRequest {

    @NotNull(message = "CON must not be null")
    @Min(value = 0, message = "CON must be zero or a positive number")
    private Integer con;

    @NotNull(message = "FR must not be null")
    @Min(value = 0, message = "FR must be zero or a positive number")
    private Integer fr;

    @NotNull(message = "DEX must not be null")
    @Min(value = 0, message = "DEX must be zero or a positive number")
    private Integer dex;

    @NotNull(message = "AGI must not be null")
    @Min(value = 0, message = "AGI must be zero or a positive number")
    private Integer agi;

    @NotNull(message = "INT must not be null")
    @Min(value = 0, message = "INT must be zero or a positive number")
    @JsonProperty("int")
    private Integer intel;

    @NotNull(message = "WILL must not be null")
    @Min(value = 0, message = "WILL must be zero or a positive number")
    private Integer will;

    @NotNull(message = "PER must not be null")
    @Min(value = 0, message = "PER must be zero or a positive number")
    private Integer per;

    @NotNull(message = "CAR must not be null")
    @Min(value = 0, message = "CAR must be zero or a positive number")
    private Integer car;


    private Integer conMod;
    private Integer frMod;
    private Integer dexMod;
    private Integer agiMod;
    private Integer intMod;
    private Integer willMod;
    private Integer perMod;
    private Integer carMod;
}
