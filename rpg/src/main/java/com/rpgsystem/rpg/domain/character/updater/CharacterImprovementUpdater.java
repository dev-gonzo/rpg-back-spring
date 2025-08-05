package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterImprovementRequest;
import com.rpgsystem.rpg.domain.character.valueObject.Cost;
import com.rpgsystem.rpg.domain.common.Name;
import com.rpgsystem.rpg.domain.entity.ImprovementEntity;

public class CharacterImprovementUpdater {

    private final CharacterImprovementRequest request;

    public CharacterImprovementUpdater(CharacterImprovementRequest request) {
        this.request = request;
    }

    public void apply(ImprovementEntity entity) {
        if (request == null || entity == null) return;

        entity.setName(Name.of(request.getName()).getValue());
        entity.setCost(Cost.of(request.getCost()).getValue());
        entity.setKitValue(Cost.of(request.getKitValue()).getValue());

    }


}
