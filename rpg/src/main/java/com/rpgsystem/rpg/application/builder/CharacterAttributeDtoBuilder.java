package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterAttributeResponse;
import com.rpgsystem.rpg.domain.entity.AttributeEntity;

public class CharacterAttributeDtoBuilder {

    public static CharacterAttributeResponse from(AttributeEntity attribute) {
        return CharacterAttributeResponse.builder()
                .characterId(attribute.getCharacter().getId())
                .con(attribute.getCon())
                .fr(attribute.getFr())
                .dex(attribute.getDex())
                .agi(attribute.getAgi())
                .intel(attribute.getIntel())
                .will(attribute.getWill())
                .per(attribute.getPer())
                .car(attribute.getCar())
                .conMod(attribute.getConMod())
                .frMod(attribute.getFrMod())
                .dexMod(attribute.getDexMod())
                .agiMod(attribute.getAgiMod())
                .intMod(attribute.getIntMod())
                .willMod(attribute.getWillMod())
                .perMod(attribute.getPerMod())
                .carMod(attribute.getCarMod())
                .build();
    }
}
