package com.rpgsystem.rpg.application.dto.character;

import com.rpgsystem.rpg.application.dto.UserSimpleDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharacterHomeDto {

    private String id;
    private String name;
    private Integer age;
    private Integer apparentAge;
    private String profession;

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

    private String image;

    private UserSimpleDto controlUser;

    private boolean edit;
}
