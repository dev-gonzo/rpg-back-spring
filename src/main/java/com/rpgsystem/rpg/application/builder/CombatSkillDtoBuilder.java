package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CombatSkillResponse;
import com.rpgsystem.rpg.domain.entity.CombatSkillEntity;

public class CombatSkillDtoBuilder {

    public static CombatSkillResponse from(CombatSkillEntity entity) {
        return CombatSkillResponse.builder()
                .id(entity.getId())
                .characterId(entity.getCharacter().getId())
                .skill(entity.getSkill())
                .group(entity.getGroup())
                .attribute(entity.getAttribute())
                .attackCost(entity.getAttackCost())
                .defenseCost(entity.getDefenseCost())
                .attackKitValue(entity.getAttackKitValue())
                .defenseKitValue(entity.getDefenseKitValue())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
