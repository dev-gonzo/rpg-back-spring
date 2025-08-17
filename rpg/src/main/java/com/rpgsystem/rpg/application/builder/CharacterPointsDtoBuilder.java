package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterPointsResponse;
import com.rpgsystem.rpg.domain.character.CharacterPoints;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;

public class CharacterPointsDtoBuilder {

    private CharacterPointsDtoBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CharacterPointsResponse from(CharacterEntity entity) {
        return CharacterPointsResponse.builder()
                .characterId(entity.getId())
                .hitPoints(entity.getHitPoints())
                .currentHitPoints(entity.getCurrentHitPoints())
                .initiative(entity.getInitiative())
                .currentInitiative(entity.getCurrentInitiative())
                .heroPoints(entity.getHeroPoints())
                .currentHeroPoints(entity.getCurrentHeroPoints())
                .magicPoints(entity.getMagicPoints())
                .currentMagicPoints(entity.getCurrentMagicPoints())
                .faithPoints(entity.getFaithPoints())
                .currentFaithPoints(entity.getCurrentFaithPoints())
                .protectionIndex(entity.getProtectionIndex())
                .currentProtectionIndex(entity.getCurrentProtectionIndex())
                .build();
    }

    public static CharacterPointsResponse from(CharacterPoints points) {
        return CharacterPointsResponse.builder()
                .characterId(points.getCharacterId().getValue())
                .hitPoints(points.getHitPoints() != null ? points.getHitPoints().getValue() : null)
                .currentHitPoints(points.getCurrentHitPoints() != null ? points.getCurrentHitPoints().getValue() : null)
                .initiative(points.getInitiative() != null ? points.getInitiative().getValue() : null)
                .currentInitiative(points.getCurrentInitiative() != null ? points.getCurrentInitiative().getValue() : null)
                .heroPoints(points.getHeroPoints() != null ? points.getHeroPoints().getValue() : null)
                .currentHeroPoints(points.getCurrentHeroPoints() != null ? points.getCurrentHeroPoints().getValue() : null)
                .magicPoints(points.getMagicPoints() != null ? points.getMagicPoints().getValue() : null)
                .currentMagicPoints(points.getCurrentMagicPoints() != null ? points.getCurrentMagicPoints().getValue() : null)
                .faithPoints(points.getFaithPoints() != null ? points.getFaithPoints().getValue() : null)
                .currentFaithPoints(points.getCurrentFaithPoints() != null ? points.getCurrentFaithPoints().getValue() : null)
                .protectionIndex(points.getProtectionIndex() != null ? points.getProtectionIndex().getValue() : null)
                .currentProtectionIndex(points.getCurrentProtectionIndex() != null ? points.getCurrentProtectionIndex().getValue() : null)
                .build();
    }
}
