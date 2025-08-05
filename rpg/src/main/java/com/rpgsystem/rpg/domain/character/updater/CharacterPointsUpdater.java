package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.domain.character.CharacterPoints;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;

public class CharacterPointsUpdater implements CharacterEntityUpdater {

    private final CharacterPoints points;

    public CharacterPointsUpdater(CharacterPoints points) {
        this.points = points;
    }

    @Override
    public void apply(CharacterEntity entity) {
        if (points == null || entity == null) return;

        if (points.getHitPoints() != null)
            entity.setHitPoints(points.getHitPoints().getValue());

        if (points.getInitiative() != null)
            entity.setInitiative(points.getInitiative().getValue());

        if (points.getHeroPoints() != null)
            entity.setHeroPoints(points.getHeroPoints().getValue());

        if (points.getMagicPoints() != null)
            entity.setMagicPoints(points.getMagicPoints().getValue());

        if (points.getFaithPoints() != null)
            entity.setFaithPoints(points.getFaithPoints().getValue());

        if (points.getProtectionIndex() != null)
            entity.setProtectionIndex(points.getProtectionIndex().getValue());
    }
}
