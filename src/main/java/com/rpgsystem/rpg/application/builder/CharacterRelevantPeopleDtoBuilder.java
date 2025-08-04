package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleResponse;
import com.rpgsystem.rpg.domain.entity.CharacterRelevantPeopleEntity;

public class CharacterRelevantPeopleDtoBuilder {

    public static CharacterRelevantPeopleResponse from(CharacterRelevantPeopleEntity entity) {
        return CharacterRelevantPeopleResponse.builder()
                .id(entity.getId())
                .characterId(entity.getCharacter().getId())
                .category(entity.getCategory())
                .name(entity.getName())
                .apparentAge(entity.getApparentAge())
                .city(entity.getCity())
                .profession(entity.getProfession())
                .briefDescription(entity.getBriefDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
