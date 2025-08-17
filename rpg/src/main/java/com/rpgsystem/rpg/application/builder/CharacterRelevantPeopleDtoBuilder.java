package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleResponse;
import com.rpgsystem.rpg.domain.entity.RelevantPeopleEntity;

public class CharacterRelevantPeopleDtoBuilder {

    private CharacterRelevantPeopleDtoBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CharacterRelevantPeopleResponse from(RelevantPeopleEntity entity) {
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
