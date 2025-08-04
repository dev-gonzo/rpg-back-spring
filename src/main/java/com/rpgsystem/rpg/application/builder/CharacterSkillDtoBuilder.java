package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterSkillResponse;
import com.rpgsystem.rpg.domain.entity.SkillEntity;

public class CharacterSkillDtoBuilder {

    public static CharacterSkillResponse from(SkillEntity entity) {
        return CharacterSkillResponse.builder()
                .id(entity.getId())
                .characterId(entity.getCharacter().getId())
                .skill(entity.getSkill())
                .group(entity.getGroup())
                .attribute(entity.getAttribute())
                .cost(entity.getCost())
                .kitValue(entity.getKitValue())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
