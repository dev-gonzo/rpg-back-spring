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

        entity.setCon(Attribute.of(request.getCon()).getValue());
        entity.setFr(Attribute.of(request.getFr()).getValue());
        entity.setDex(Attribute.of(request.getDex()).getValue());
        entity.setAgi(Attribute.of(request.getAgi()).getValue());
        entity.setIntel(Attribute.of(request.getIntel()).getValue());
        entity.setWill(Attribute.of(request.getWill()).getValue());
        entity.setPer(Attribute.of(request.getPer()).getValue());
        entity.setCar(Attribute.of(request.getCar()).getValue());
    }


    private Integer unwrap(Modifier modifier) {
        return modifier != null ? modifier.getValue() : null;
    }
}
