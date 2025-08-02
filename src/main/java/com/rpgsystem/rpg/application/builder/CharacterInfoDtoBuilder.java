package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.application.dto.character.CharacterInfoDto;
import com.rpgsystem.rpg.domain.character.CharacterInfo;
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
                .apparentAge(c.getApparentAge())
                .heightCm(c.getHeightCm())
                .weightKg(c.getWeightKg())
                .religion(c.getReligion())
                .build();
    }


    public static CharacterInfoDto from(CharacterInfo characterInfo, String id) {
        return CharacterInfoDto.builder()
                .id(id)
                .name(characterInfo.getName() != null ? characterInfo.getName().getValue() : null)
                .profession(characterInfo.getProfession())
                .birthDate(characterInfo.getBirthDate())
                .birthPlace(characterInfo.getBirthPlace())
                .gender(characterInfo.getGender())
                .age(characterInfo.getAge())
                .apparentAge(characterInfo.getApparenteAge())
                .heightCm(characterInfo.getHeightCm() != null ? characterInfo.getHeightCm().getCentimeters() : null)
                .weightKg(characterInfo.getWeightKg() != null ? characterInfo.getWeightKg().getKilograms() : null)
                .religion(characterInfo.getReligion())
                .build();
    }


    public static CharacterInfoDto from(CharacterInfo characterInfo) {
        return CharacterInfoDto.builder()
                .name(characterInfo.getName() != null ? characterInfo.getName().getValue() : null)
                .profession(characterInfo.getProfession())
                .birthDate(characterInfo.getBirthDate())
                .birthPlace(characterInfo.getBirthPlace())
                .gender(characterInfo.getGender())
                .age(characterInfo.getAge())
                .apparentAge(characterInfo.getApparenteAge())
                .heightCm(characterInfo.getHeightCm() != null ? characterInfo.getHeightCm().getCentimeters() : null)
                .weightKg(characterInfo.getWeightKg() != null ? characterInfo.getWeightKg().getKilograms() : null)
                .religion(characterInfo.getReligion())
                .build();
    }
}
