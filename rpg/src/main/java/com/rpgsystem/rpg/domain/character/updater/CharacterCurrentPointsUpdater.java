package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.domain.character.CharacterPoints;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;

public class CharacterCurrentPointsUpdater implements CharacterEntityUpdater {

    private final CharacterPoints points;

    public CharacterCurrentPointsUpdater(CharacterPoints points) {
        this.points = points;
    }

    @Override
    public void apply(CharacterEntity entity) {
        if (points == null || entity == null) return;

        if (points.getCurrentHitPoints() != null)
            entity.setCurrentHitPoints(points.getCurrentHitPoints().getValue());

        if (points.getCurrentInitiative() != null)
            entity.setCurrentInitiative(points.getCurrentInitiative().getValue());

        if (points.getCurrentHeroPoints() != null)
            entity.setCurrentHeroPoints(points.getCurrentHeroPoints().getValue());

        if (points.getCurrentMagicPoints() != null)
            entity.setCurrentMagicPoints(points.getCurrentMagicPoints().getValue());

        if (points.getCurrentFaithPoints() != null)
            entity.setCurrentFaithPoints(points.getCurrentFaithPoints().getValue());

        if (points.getCurrentProtectionIndex() != null)
            entity.setCurrentProtectionIndex(points.getCurrentProtectionIndex().getValue());
    }
}
