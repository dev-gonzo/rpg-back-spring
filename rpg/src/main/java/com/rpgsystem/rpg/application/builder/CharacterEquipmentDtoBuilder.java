package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentResponse;
import com.rpgsystem.rpg.domain.entity.EquipmentEntity;

public class CharacterEquipmentDtoBuilder {

    private CharacterEquipmentDtoBuilder() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static CharacterEquipmentResponse from(EquipmentEntity entity) {
        return CharacterEquipmentResponse.builder()
                .id(entity.getId())
                .characterId(entity.getCharacter().getId())
                .name(entity.getName())
                .quantity(entity.getQuantity())
                .classification(entity.getClassification())
                .description(entity.getDescription())
                .kineticProtection(entity.getKineticProtection())
                .ballisticProtection(entity.getBallisticProtection())
                .dexterityPenalty(entity.getDexterityPenalty())
                .agilityPenalty(entity.getAgilityPenalty())
                .initiative(entity.getInitiative())
                .bookPage(entity.getBookPage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
