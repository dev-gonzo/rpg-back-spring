package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterAttributeRequest;
import com.rpgsystem.rpg.domain.character.valueObject.Attribute;
import com.rpgsystem.rpg.domain.character.valueObject.Modifier;
import com.rpgsystem.rpg.domain.entity.AttributeEntity;

public class CharacterAttributesUpdater {

    private final CharacterAttributeRequest request;

    public CharacterAttributesUpdater(CharacterAttributeRequest request) {
        this.request = request;
    }

    public void apply(AttributeEntity entity) {
        if (request == null || entity == null) return;

        entity.setCon(request.getCon() != null ? Attribute.of(request.getCon()).getValue() : null);
        entity.setFr(request.getFr() != null ? Attribute.of(request.getFr()).getValue() : null);
        entity.setDex(request.getDex() != null ? Attribute.of(request.getDex()).getValue() : null);
        entity.setAgi(request.getAgi() != null ? Attribute.of(request.getAgi()).getValue() : null);
        entity.setIntel(request.getIntel() != null ? Attribute.of(request.getIntel()).getValue() : null);
        entity.setWill(request.getWill() != null ? Attribute.of(request.getWill()).getValue() : null);
        entity.setPer(request.getPer() != null ? Attribute.of(request.getPer()).getValue() : null);
        entity.setCar(request.getCar() != null ? Attribute.of(request.getCar()).getValue() : null);

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
