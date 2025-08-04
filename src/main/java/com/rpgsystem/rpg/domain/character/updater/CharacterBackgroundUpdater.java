package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundRequest;
import com.rpgsystem.rpg.domain.entity.BackgroundEntity;

public class CharacterBackgroundUpdater {

    private final CharacterBackgroundRequest request;

    public CharacterBackgroundUpdater(CharacterBackgroundRequest request) {
        this.request = request;
    }

    public void apply(BackgroundEntity entity) {
        if (request == null || entity == null) return;

        entity.setTitle(request.getTitle());
        entity.setText(request.getText());
        entity.setPublic(request.isPublic());
    }
}
