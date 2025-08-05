package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.character.valueObject.Point;
import com.rpgsystem.rpg.domain.common.CodigoId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CharacterPoints {

    private final CodigoId characterId;

    private final Point hitPoints;
    private final Point currentHitPoints;

    private final Point initiative;
    private final Point currentInitiative;

    private final Point heroPoints;
    private final Point currentHeroPoints;

    private final Point magicPoints;
    private final Point currentMagicPoints;

    private final Point faithPoints;
    private final Point currentFaithPoints;

    private final Point protectionIndex;
    private final Point currentProtectionIndex;

    public static CharacterPoints fromBasePoints(
            CodigoId characterId,
            Point hitPoints,
            Point initiative,
            Point heroPoints,
            Point magicPoints,
            Point faithPoints,
            Point protectionIndex
    ) {
        return new CharacterPoints(
                characterId,
                hitPoints,
                null,
                initiative,
                null,
                heroPoints,
                null,
                magicPoints,
                null,
                faithPoints,
                null,
                protectionIndex,
                null
        );
    }

    public static CharacterPoints fromCurrentPoints(
            CodigoId characterId,
            Point currentHitPoints,
            Point currentInitiative,
            Point currentHeroPoints,
            Point currentMagicPoints,
            Point currentFaithPoints,
            Point currentProtectionIndex
    ) {
        return new CharacterPoints(
                characterId,
                null,
                currentHitPoints,
                null,
                currentInitiative,
                null,
                currentHeroPoints,
                null,
                currentMagicPoints,
                null,
                currentFaithPoints,
                null,
                currentProtectionIndex
        );
    }
}
