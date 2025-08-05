package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterCurrentPointsRequest;
import com.rpgsystem.rpg.domain.character.valueObject.Point;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;

public class CharacterCurrentPatchPointsUpdater implements CharacterEntityUpdater {
    private final CharacterCurrentPointsRequest request;

    public CharacterCurrentPatchPointsUpdater(CharacterCurrentPointsRequest request) {
        this.request = request;
    }

    @Override
    public void apply(CharacterEntity entity) {
        if (request == null || entity == null) return;

        if (request.getCurrentHitPoints() != null) {
            entity.setCurrentHitPoints(Point.of(request.getCurrentHitPoints()).getValue());
        }

        if (request.getCurrentInitiative() != null) {
            entity.setCurrentInitiative(Point.of(request.getCurrentInitiative()).getValue());
        }

        if (request.getCurrentHeroPoints() != null) {
            entity.setCurrentHeroPoints(Point.of(request.getCurrentHeroPoints()).getValue());
        }

        if (request.getCurrentMagicPoints() != null) {
            entity.setCurrentMagicPoints(Point.of(request.getCurrentMagicPoints()).getValue());
        }

        if (request.getCurrentFaithPoints() != null) {
            entity.setCurrentFaithPoints(Point.of(request.getCurrentFaithPoints()).getValue());
        }

        if (request.getCurrentProtectionIndex() != null) {
            entity.setCurrentProtectionIndex(Point.of(request.getCurrentProtectionIndex()).getValue());
        }
    }
}
