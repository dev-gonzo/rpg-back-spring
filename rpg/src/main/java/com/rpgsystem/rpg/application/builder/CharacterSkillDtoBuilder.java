package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterSkillResponse;
import com.rpgsystem.rpg.domain.entity.SkillEntity;

public class CharacterSkillDtoBuilder {

    private CharacterSkillDtoBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CharacterSkillResponse build(SkillEntity entity) {
        return CharacterSkillResponse.builder()
                .id(entity.getId())
                .characterId(entity.getCharacter().getId())
                .skill(entity.getName())
                .group(null) // Campo não existe na entidade
                .attribute(null) // Campo não existe na entidade
                .cost(null) // Campo não existe na entidade
                .kitValue(null) // Campo não existe na entidade
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
