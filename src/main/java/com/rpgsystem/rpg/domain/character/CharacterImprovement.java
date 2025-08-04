package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.character.valueObject.Cost;
import com.rpgsystem.rpg.domain.common.CodigoId;
import com.rpgsystem.rpg.domain.common.Name;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class CharacterImprovement {

    private final CodigoId characterId;

    private final Name name;
    private final Cost cost;

    private final Cost kitValue;

    private final LocalDate createdAt;
    private final LocalDate updatedAt;


}
