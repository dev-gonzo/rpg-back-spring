package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterPointsRequest {

    private Integer hitPoints;

    private Integer initiative;

    private Integer heroPoints;

    private Integer magicPoints;

    private Integer faithPoints;

    private Integer protectionIndex;
}
