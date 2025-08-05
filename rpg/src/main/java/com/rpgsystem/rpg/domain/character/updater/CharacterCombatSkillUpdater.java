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

        entity.setSkill(Name.of(request.getSkill()).getValue());
        entity.setGroup(request.getGroup());
        entity.setAttribute(AttributeLabel.of(request.getAttribute()).getValue().name());

        entity.setAttackCost(Cost.of(request.getAttackCost()).getValue());
        entity.setDefenseCost(Cost.of(request.getDefenseCost()).getValue());

        entity.setAttackKitValue(Cost.of(request.getAttackKitValue()).getValue());
        entity.setDefenseKitValue(Cost.of(request.getDefenseKitValue()).getValue());
    }
}
