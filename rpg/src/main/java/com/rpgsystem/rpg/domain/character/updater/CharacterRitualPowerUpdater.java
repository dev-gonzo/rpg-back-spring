package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerRequest;
import com.rpgsystem.rpg.domain.common.Name;
import com.rpgsystem.rpg.domain.entity.RitualPowerEntity;

public class CharacterRitualPowerUpdater {

    private final CharacterRitualPowerRequest request;

    public CharacterRitualPowerUpdater(CharacterRitualPowerRequest request) {
        this.request = request;
    }

    public void apply(RitualPowerEntity entity) {
        if (request == null || entity == null) return;

        entity.setName(Name.of(request.getName()).getValue());
        entity.setPathsForms(request.getPathsForms());
        entity.setDescription(request.getDescription());
        entity.setBookPage(request.getBookPage());
    }
}
