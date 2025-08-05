package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.character.valueObject.Attribute;
import com.rpgsystem.rpg.domain.character.valueObject.Modifier;
import com.rpgsystem.rpg.domain.common.CodigoId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CharacterAttributes {

    private final CodigoId id;
    private final CodigoId characterId;

    private final Attribute con;
    private final Attribute fr;
    private final Attribute dex;
    private final Attribute agi;
    private final Attribute intel;
    private final Attribute will;
    private final Attribute per;
    private final Attribute car;

    private final Modifier conMod;
    private final Modifier frMod;
    private final Modifier dexMod;
    private final Modifier agiMod;
    private final Modifier intMod;
    private final Modifier willMod;
    private final Modifier perMod;
    private final Modifier carMod;
}
