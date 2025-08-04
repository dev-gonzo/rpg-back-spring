package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterImprovementResponse;
import com.rpgsystem.rpg.domain.entity.ImprovementEntity;

public class CharacterImprovementDtoBuilder {

    public static CharacterImprovementResponse from(ImprovementEntity entity) {
        return CharacterImprovementResponse.builder()
                .characterId(entity.getCharacter().getId())
                .cost(entity.getCost())
                .kitCost(entity.getCost())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
