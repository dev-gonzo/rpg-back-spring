package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillResponse;
import com.rpgsystem.rpg.domain.entity.CombatSkillEntity;

public class CombatSkillDtoBuilder {

    private CombatSkillDtoBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CharacterCombatSkillResponse build(CombatSkillEntity combatSkillEntity) {
        return CharacterCombatSkillResponse.builder()
                .id(combatSkillEntity.getId())
                .characterId(combatSkillEntity.getCharacter().getId())
                .skill(combatSkillEntity.getName())
                .group(null) // Campo não existe na entidade
                .attribute(null) // Campo não existe na entidade
                .attackCost(null) // Campo não existe na entidade
                .defenseCost(null) // Campo não existe na entidade
                .attackKitValue(null) // Campo não existe na entidade
                .defenseKitValue(null) // Campo não existe na entidade
                .createdAt(combatSkillEntity.getCreatedAt())
                .updatedAt(combatSkillEntity.getUpdatedAt())
                .build();
    }
}
