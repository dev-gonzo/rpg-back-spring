package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterWeaponResponse;
import com.rpgsystem.rpg.domain.entity.WeaponEntity;

public class CharacterWeaponDtoBuilder {

    private CharacterWeaponDtoBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CharacterWeaponResponse from(WeaponEntity entity) {
        return CharacterWeaponResponse.builder()
                .id(entity.getId())
                .characterId(entity.getCharacter().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .damage(entity.getDamage())
                .initiative(entity.getInitiative())
                .range(entity.getRange())
                .rof(entity.getRof())
                .ammunition(entity.getAmmunition())
                .bookPage(entity.getBookPage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
