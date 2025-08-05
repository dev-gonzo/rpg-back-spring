package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.character.valueObject.CharacterName;
import com.rpgsystem.rpg.domain.character.valueObject.Height;
import com.rpgsystem.rpg.domain.character.valueObject.Weight;
import com.rpgsystem.rpg.domain.common.CodigoId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class CharacterCharacterInfo {

    private final CodigoId characterId;
    private final CharacterName name;
    private final String profession;
    private final LocalDate birthDate;
    private final String birthPlace;
    private final String gender;
    private final Integer age;
    private final Integer apparenteAge;
    private final Height heightCm;
    private final Weight weightKg;
    private final String religion;
}
