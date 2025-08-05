package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.domain.character.CharacterCharacterInfo;
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


    public static CharacterInfoResponse from(CharacterCharacterInfo characterCharacterInfo, String id) {
        return CharacterInfoResponse.builder()
                .id(id)
                .name(characterCharacterInfo.getName() != null ? characterCharacterInfo.getName().getValue() : null)
                .profession(characterCharacterInfo.getProfession())
                .birthDate(characterCharacterInfo.getBirthDate())
                .birthPlace(characterCharacterInfo.getBirthPlace())
                .gender(characterCharacterInfo.getGender())
                .age(characterCharacterInfo.getAge())
                .apparentAge(characterCharacterInfo.getApparenteAge())
                .heightCm(characterCharacterInfo.getHeightCm() != null ? characterCharacterInfo.getHeightCm().getCentimeters() : null)
                .weightKg(characterCharacterInfo.getWeightKg() != null ? characterCharacterInfo.getWeightKg().getKilograms() : null)
                .religion(characterCharacterInfo.getReligion())
                .build();
    }


    public static CharacterInfoResponse from(CharacterCharacterInfo characterCharacterInfo) {
        return CharacterInfoResponse.builder()
                .name(characterCharacterInfo.getName() != null ? characterCharacterInfo.getName().getValue() : null)
                .profession(characterCharacterInfo.getProfession())
                .birthDate(characterCharacterInfo.getBirthDate())
                .birthPlace(characterCharacterInfo.getBirthPlace())
                .gender(characterCharacterInfo.getGender())
                .age(characterCharacterInfo.getAge())
                .apparentAge(characterCharacterInfo.getApparenteAge())
                .heightCm(characterCharacterInfo.getHeightCm() != null ? characterCharacterInfo.getHeightCm().getCentimeters() : null)
                .weightKg(characterCharacterInfo.getWeightKg() != null ? characterCharacterInfo.getWeightKg().getKilograms() : null)
                .religion(characterCharacterInfo.getReligion())
                .build();
    }


}
