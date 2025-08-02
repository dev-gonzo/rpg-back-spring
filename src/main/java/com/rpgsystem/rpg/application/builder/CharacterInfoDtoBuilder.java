package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.application.dto.character.CharacterInfoDto;
import com.rpgsystem.rpg.domain.entity.Character;

public class CharacterInfoDtoBuilder {

    public static CharacterInfoDto from(Character c) {
        return CharacterInfoDto.builder()
                .id(c.getId())
                .name(c.getName())
                .profession(c.getProfession())
                .birthDate(c.getBirthDate())
                .birthPlace(c.getBirthPlace())
                .gender(c.getGender())
                .age(c.getAge())
                .apparenteAge(c.getApparentAge())
                .heightCm(c.getHeightCm())
                .weightKg(c.getWeightKg())
                .religion(c.getReligion())
                .build();
    }
}
