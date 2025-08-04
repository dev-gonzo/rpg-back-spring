package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleRequest;
import com.rpgsystem.rpg.domain.common.Name;
import com.rpgsystem.rpg.domain.entity.CharacterRelevantPeopleEntity;

public class CharacterRelevantPeopleUpdater {

    private final CharacterRelevantPeopleRequest request;

    public CharacterRelevantPeopleUpdater(CharacterRelevantPeopleRequest request) {
        this.request = request;
    }

    public void apply(CharacterRelevantPeopleEntity entity) {
        if (request == null || entity == null) return;

        entity.setCategory(request.getCategory());
        entity.setName(Name.of(request.getName()).getValue());
        entity.setApparentAge(request.getApparentAge());
        entity.setCity(request.getCity());
        entity.setProfession(request.getProfession());
        entity.setBriefDescription(request.getBriefDescription());
    }
}
