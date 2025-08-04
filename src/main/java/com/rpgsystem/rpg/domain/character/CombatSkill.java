package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.character.valueObject.AttributeLabel;
import com.rpgsystem.rpg.domain.character.valueObject.Cost;
import com.rpgsystem.rpg.domain.common.CodigoId;
import com.rpgsystem.rpg.domain.common.Name;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class CombatSkill {

    private final CodigoId id;
    private final CodigoId characterId;

    private final String group;
    private final Name skill;
    private final AttributeLabel attribute;

    private final Cost attackCost;
    private final Cost defenseCost;

    private final Cost attackKitValue;
    private final Cost defenseKitValue;

    private final LocalDate createdAt;
    private final LocalDate updatedAt;
}
