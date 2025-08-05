package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundResponse;
import com.rpgsystem.rpg.domain.entity.BackgroundEntity;

public class CharacterBackgroundDtoBuilder {

    public static CharacterBackgroundResponse from(BackgroundEntity entity) {
        return CharacterBackgroundResponse.builder()
                .id(entity.getId())
                .characterId(entity.getCharacter().getId())
                .title(entity.getTitle())
                .text(entity.getText())
                .isPublic(entity.isPublic())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
