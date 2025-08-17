package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillRequest;
import com.rpgsystem.rpg.domain.character.valueObject.AttributeLabel;
import com.rpgsystem.rpg.domain.character.valueObject.Cost;
import com.rpgsystem.rpg.domain.common.Name;
import com.rpgsystem.rpg.domain.entity.CombatSkillEntity;

public class CharacterCombatSkillUpdater {

    private final CharacterCombatSkillRequest request;

    public CharacterCombatSkillUpdater(CharacterCombatSkillRequest request) {
        this.request = request;
    }

    public void apply(CombatSkillEntity entity) {
        if (request == null || entity == null) return;

        entity.setName(Name.of(request.getSkill()).getValue());
        entity.setSkillValue(0); // Default value since skillValue is not in request
        entity.setBookPage(null); // Default value since bookPage is not in request
    }
}
