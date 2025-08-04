package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerResponse;
import com.rpgsystem.rpg.domain.entity.RitualPowerEntity;

public class CharacterRitualPowerDtoBuilder {

    public static CharacterRitualPowerResponse from(RitualPowerEntity entity) {
        return CharacterRitualPowerResponse.builder()
                .id(entity.getId())
                .characterId(entity.getCharacter().getId())
                .name(entity.getName())
                .pathsForms(entity.getPathsForms())
                .description(entity.getDescription())
                .bookPage(entity.getBookPage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
