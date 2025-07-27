package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.application.dto.CharacterHomeDto;
import com.rpgsystem.rpg.domain.model.Character;

public class CharacterHomeDtoBuilder {

    public static CharacterHomeDto from(Character c) {
        return CharacterHomeDto.builder()
                .id(c.getId())
                .name(c.getName())
                .age(c.getAge())
                .apparentAge(c.getApparentAge())
                .profession(c.getProfession())
                .hitPoints(c.getHitPoints())
                .currentHitPoints(c.getCurrentHitPoints())
                .initiative(c.getInitiative())
                .currentInitiative(c.getCurrentInitiative())
                .heroPoints(c.getHeroPoints())
                .currentHeroPoints(c.getCurrentHeroPoints())
                .magicPoints(c.getMagicPoints())
                .currentMagicPoints(c.getCurrentMagicPoints())
                .faithPoints(c.getFaithPoints())
                .currentFaithPoints(c.getCurrentFaithPoints())
                .protectionIndex(c.getProtectionIndex())
                .currentProtectionIndex(c.getCurrentProtectionIndex())
                .image(c.getImage())
                .controlUser(UserSimpleDtoBuilder.build(c.getControlUser()))
                .edit(c.isEdit())
                .build();
    }
}
