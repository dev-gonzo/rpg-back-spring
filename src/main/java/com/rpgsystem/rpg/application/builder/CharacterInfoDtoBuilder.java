package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterAttributeResponse;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.domain.character.CharacterInfo;
import com.rpgsystem.rpg.domain.entity.AttributeEntity;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;

public class CharacterInfoDtoBuilder {

    public static CharacterInfoResponse from(CharacterEntity c) {
        return CharacterInfoResponse.builder()
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


    public static CharacterInfoResponse from(CharacterInfo characterInfo, String id) {
        return CharacterInfoResponse.builder()
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


    public static CharacterInfoResponse from(CharacterInfo characterInfo) {
        return CharacterInfoResponse.builder()
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
