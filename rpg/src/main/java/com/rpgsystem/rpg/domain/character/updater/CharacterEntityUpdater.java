package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.domain.entity.CharacterEntity;

public interface CharacterEntityUpdater {
    void apply(CharacterEntity entity);
}
