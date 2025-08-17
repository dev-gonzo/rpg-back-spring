package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.domain.character.CharacterCharacterInfo;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;

public class CharacterInfoDtoBuilder {

    private CharacterInfoDtoBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CharacterInfoResponse from(CharacterEntity c) {
        return CharacterInfoResponse.builder()
                .id(c.getId() != null ? c.getId() : null)
                .name(c.getName() != null ? c.getName() : null)
                .profession(c.getProfession() != null ? c.getProfession() : null)
                .birthDate(c.getBirthDate() != null ? c.getBirthDate() : null)
                .birthPlace(c.getBirthPlace() != null ? c.getBirthPlace() : null)
                .gender(c.getGender() != null ? c.getGender() : null)
                .age(c.getAge() != null ? c.getAge() : null)
                .apparentAge(c.getApparentAge() != null ? c.getApparentAge() : null)
                .heightCm(c.getHeightCm() != null ? c.getHeightCm() : null)
                .weightKg(c.getWeightKg() != null ? c.getWeightKg() : null)
                .religion(c.getReligion() != null ? c.getReligion() : null)
                .build();
    }



    public static CharacterInfoResponse from(CharacterCharacterInfo characterCharacterInfo, String id) {
        return CharacterInfoResponse.builder()
                .id(id)
                .name(characterCharacterInfo.getName() != null ? characterCharacterInfo.getName().getValue() : null)
                .profession(characterCharacterInfo.getProfession() != null ? characterCharacterInfo.getProfession() : null)
                .birthDate(characterCharacterInfo.getBirthDate() != null ? characterCharacterInfo.getBirthDate() : null)
                .birthPlace(characterCharacterInfo.getBirthPlace() != null ? characterCharacterInfo.getBirthPlace() : null)
                .gender(characterCharacterInfo.getGender() != null ? characterCharacterInfo.getGender() : null)
                .age(characterCharacterInfo.getAge() != null ? characterCharacterInfo.getAge() : null)
                .apparentAge(characterCharacterInfo.getApparenteAge() != null ? characterCharacterInfo.getApparenteAge() : null)
                .heightCm(characterCharacterInfo.getHeightCm() != null ? characterCharacterInfo.getHeightCm().getCentimeters() : null)
                .weightKg(characterCharacterInfo.getWeightKg() != null ? characterCharacterInfo.getWeightKg().getKilograms() : null)
                .religion(characterCharacterInfo.getReligion() != null ? characterCharacterInfo.getReligion() : null)
                .build();
    }



    public static CharacterInfoResponse from(CharacterCharacterInfo characterCharacterInfo) {
        return CharacterInfoResponse.builder()
                .name(characterCharacterInfo.getName() != null ? characterCharacterInfo.getName().getValue() : null)
                .profession(characterCharacterInfo.getProfession() != null ? characterCharacterInfo.getProfession() : null)
                .birthDate(characterCharacterInfo.getBirthDate() != null ? characterCharacterInfo.getBirthDate() : null)
                .birthPlace(characterCharacterInfo.getBirthPlace() != null ? characterCharacterInfo.getBirthPlace() : null)
                .gender(characterCharacterInfo.getGender() != null ? characterCharacterInfo.getGender() : null)
                .age(characterCharacterInfo.getAge() != null ? characterCharacterInfo.getAge() : null)
                .apparentAge(characterCharacterInfo.getApparenteAge() != null ? characterCharacterInfo.getApparenteAge() : null)
                .heightCm(characterCharacterInfo.getHeightCm() != null ? characterCharacterInfo.getHeightCm().getCentimeters() : null)
                .weightKg(characterCharacterInfo.getWeightKg() != null ? characterCharacterInfo.getWeightKg().getKilograms() : null)
                .religion(characterCharacterInfo.getReligion() != null ? characterCharacterInfo.getReligion() : null)
                .build();
    }



}
