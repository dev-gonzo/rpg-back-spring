package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentRequest;
import com.rpgsystem.rpg.domain.common.Name;
import com.rpgsystem.rpg.domain.entity.EquipmentEntity;

public class CharacterEquipmentUpdater {

    private final CharacterEquipmentRequest request;

    public CharacterEquipmentUpdater(CharacterEquipmentRequest request) {
        this.request = request;
    }

    public void apply(EquipmentEntity entity) {
        if (request == null || entity == null) return;

        entity.setName(Name.of(request.getName()).getValue());
        entity.setQuantity(request.getQuantity());
        entity.setClassification(request.getClassification());
        entity.setDescription(request.getDescription());
        entity.setKineticProtection(request.getKineticProtection());
        entity.setBallisticProtection(request.getBallisticProtection());
        entity.setDexterityPenalty(request.getDexterityPenalty());
        entity.setAgilityPenalty(request.getAgilityPenalty());
        entity.setInitiative(request.getInitiative());
        entity.setBookPage(request.getBookPage());
    }
}
