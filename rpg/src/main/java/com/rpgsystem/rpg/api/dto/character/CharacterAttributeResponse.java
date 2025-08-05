package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterAttributeResponse {

    private String characterId;

    private Integer con;
    private Integer fr;
    private Integer dex;
    private Integer agi;
    private Integer intel;
    private Integer will;
    private Integer per;
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
