package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsRequest;
import com.rpgsystem.rpg.domain.character.valueObject.Focus;
import com.rpgsystem.rpg.domain.entity.PathsAndFormsEntity;

public class CharacterPathsAndFormsUpdater {

    private final CharacterPathsAndFormsRequest request;

    public CharacterPathsAndFormsUpdater(CharacterPathsAndFormsRequest request) {
        this.request = request;
    }

    public void apply(PathsAndFormsEntity entity) {
        if (request == null || entity == null) return;

        entity.setUnderstandForm(Focus.ofForm(request.getUnderstandForm()).getValue());
        entity.setCreateForm(Focus.ofForm(request.getCreateForm()).getValue());
        entity.setControlForm(Focus.ofForm(request.getControlForm()).getValue());

        entity.setFire(Focus.ofPath(request.getFire()).getValue());
        entity.setWater(Focus.ofPath(request.getWater()).getValue());
        entity.setEarth(Focus.ofPath(request.getEarth()).getValue());
        entity.setAir(Focus.ofPath(request.getAir()).getValue());
        entity.setLight(Focus.ofPath(request.getLight()).getValue());
        entity.setDarkness(Focus.ofPath(request.getDarkness()).getValue());
        entity.setPlants(Focus.ofPath(request.getPlants()).getValue());
        entity.setAnimals(Focus.ofPath(request.getAnimals()).getValue());
        entity.setHumans(Focus.ofPath(request.getHumans()).getValue());
        entity.setSpiritum(Focus.ofPath(request.getSpiritum()).getValue());
        entity.setArkanun(Focus.ofPath(request.getArkanun()).getValue());
        entity.setMetamagic(Focus.ofPath(request.getMetamagic()).getValue());
    }
}
