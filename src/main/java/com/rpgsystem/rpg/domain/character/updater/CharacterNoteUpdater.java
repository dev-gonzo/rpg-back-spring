package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterNoteRequest;
import com.rpgsystem.rpg.domain.entity.NoteEntity;

public class CharacterNoteUpdater {

    private final CharacterNoteRequest request;

    public CharacterNoteUpdater(CharacterNoteRequest request) {
        this.request = request;
    }

    public void apply(NoteEntity entity) {
        if (request == null || entity == null) return;
        entity.setNote(request.getNote());
    }
}
