package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterNoteResponse;
import com.rpgsystem.rpg.domain.entity.NoteEntity;

public class CharacterNoteDtoBuilder {

    public static CharacterNoteResponse from(NoteEntity entity) {
        return CharacterNoteResponse.builder()
                .id(entity.getId())
                .characterId(entity.getCharacter().getId())
                .note(entity.getNote())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
