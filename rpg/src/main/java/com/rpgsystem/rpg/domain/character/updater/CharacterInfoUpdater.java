package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.domain.character.CharacterCharacterInfo;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;

public class CharacterInfoUpdater implements CharacterEntityUpdater {

    private final CharacterCharacterInfo info;

    public CharacterInfoUpdater(CharacterCharacterInfo info) {
        this.info = info;
    }

    @Override
    public void apply(CharacterEntity entity) {
        if (info == null || entity == null) return;

        entity.setName(info.getName() != null ? info.getName().getValue() : null);
        entity.setProfession(info.getProfession() != null ? info.getProfession() : null);
        entity.setBirthDate(info.getBirthDate() != null ? info.getBirthDate() : null);
        entity.setBirthPlace(info.getBirthPlace() != null ? info.getBirthPlace() : null);
        entity.setGender(info.getGender() != null ? info.getGender() : null);
        entity.setAge(info.getAge() != null ? info.getAge() : null);
        entity.setApparentAge(info.getApparenteAge() != null ? info.getApparenteAge() : null);
        entity.setHeightCm(info.getHeightCm() != null ? info.getHeightCm().getCentimeters() : null);
        entity.setWeightKg(info.getWeightKg() != null ? info.getWeightKg().getKilograms() : null);
        entity.setReligion(info.getReligion() != null ? info.getReligion() : null);
    }

}
