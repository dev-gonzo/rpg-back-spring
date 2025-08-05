package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterWeaponRequest;
import com.rpgsystem.rpg.domain.common.Name;
import com.rpgsystem.rpg.domain.entity.WeaponEntity;

public class CharacterWeaponUpdater {

    private final CharacterWeaponRequest request;

    public CharacterWeaponUpdater(CharacterWeaponRequest request) {
        this.request = request;
    }

    public void apply(WeaponEntity entity) {
        if (request == null || entity == null) return;

        entity.setName(Name.of(request.getName()).getValue());
        entity.setDescription(request.getDescription());
        entity.setDamage(request.getDamage());
        entity.setInitiative(request.getInitiative());
        entity.setRange(request.getRange());
        entity.setRof(request.getRof());
        entity.setAmmunition(request.getAmmunition());
        entity.setBookPage(request.getBookPage());
    }
}
