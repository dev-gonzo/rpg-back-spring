package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterPointsResponse {

    private String characterId;

    private Integer hitPoints;
    private Integer currentHitPoints;

    private Integer initiative;
    private Integer currentInitiative;

    private Integer heroPoints;
    private Integer currentHeroPoints;

    private Integer magicPoints;
    private Integer currentMagicPoints;

    private Integer faithPoints;
    private Integer currentFaithPoints;

    private Integer protectionIndex;
    private Integer currentProtectionIndex;
}
