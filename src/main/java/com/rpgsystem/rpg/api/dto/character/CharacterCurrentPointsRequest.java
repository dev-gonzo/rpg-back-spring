package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterCurrentPointsRequest {


    private Integer currentHitPoints;

    private Integer currentInitiative;

    private Integer currentHeroPoints;

    private Integer currentMagicPoints;

    private Integer currentFaithPoints;

    private Integer currentProtectionIndex;
}
