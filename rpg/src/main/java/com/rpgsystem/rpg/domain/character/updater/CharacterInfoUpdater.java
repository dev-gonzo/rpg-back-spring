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

        entity.setName(info.getName().getValue());
        entity.setProfession(info.getProfession());
        entity.setBirthDate(info.getBirthDate());
        entity.setBirthPlace(info.getBirthPlace());
        entity.setGender(info.getGender());
        entity.setAge(info.getAge());
        entity.setApparentAge(info.getApparenteAge());
        entity.setHeightCm(info.getHeightCm() != null ? info.getHeightCm().getCentimeters() : null);
        entity.setWeightKg(info.getWeightKg() != null ? info.getWeightKg().getKilograms() : null);
        entity.setReligion(info.getReligion());
    }
}
