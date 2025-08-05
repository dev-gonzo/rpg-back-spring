package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterSkillRequest;
import com.rpgsystem.rpg.domain.character.valueObject.AttributeLabel;
import com.rpgsystem.rpg.domain.character.valueObject.Cost;
import com.rpgsystem.rpg.domain.common.Name;
import com.rpgsystem.rpg.domain.entity.SkillEntity;

public class CharacterSkillUpdater {

    private final CharacterSkillRequest request;

    public CharacterSkillUpdater(CharacterSkillRequest request) {
        this.request = request;
    }

    public void apply(SkillEntity entity) {
        if (request == null || entity == null) return;

        entity.setSkill(Name.of(request.getSkill()).getValue());
        entity.setGroup(request.getGroup());
        entity.setAttribute(AttributeLabel.of(request.getAttribute()).getValue().name());
        entity.setCost(Cost.of(request.getCost()).getValue());
        entity.setKitValue(Cost.of(request.getKitValue()).getValue());
    }
}
