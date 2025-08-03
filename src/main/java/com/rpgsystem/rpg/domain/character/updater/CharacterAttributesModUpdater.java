package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterAttributeModRequest;
import com.rpgsystem.rpg.domain.character.valueObject.Modifier;
import com.rpgsystem.rpg.domain.entity.AttributeEntity;

public class CharacterAttributesModUpdater {

    private final CharacterAttributeModRequest request;

    public CharacterAttributesModUpdater(CharacterAttributeModRequest request) {
        this.request = request;
    }

    public void apply(AttributeEntity entity) {
        if (request == null || entity == null) return;

        entity.setConMod(toValue(request.getConMod()));
        entity.setFrMod(toValue(request.getFrMod()));
        entity.setDexMod(toValue(request.getDexMod()));
        entity.setAgiMod(toValue(request.getAgiMod()));
        entity.setIntMod(toValue(request.getIntMod()));
        entity.setWillMod(toValue(request.getWillMod()));
        entity.setPerMod(toValue(request.getPerMod()));
        entity.setCarMod(toValue(request.getCarMod()));
    }

    private Integer toValue(Integer value) {
        Modifier mod = Modifier.of(value);
        return mod != null ? mod.getValue() : null;
    }
}
