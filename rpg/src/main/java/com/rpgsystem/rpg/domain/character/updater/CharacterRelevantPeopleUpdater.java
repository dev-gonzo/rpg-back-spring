package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleRequest;
import com.rpgsystem.rpg.domain.common.Name;
import com.rpgsystem.rpg.domain.entity.RelevantPeopleEntity;

public class CharacterRelevantPeopleUpdater {

    private final CharacterRelevantPeopleRequest request;

    public CharacterRelevantPeopleUpdater(CharacterRelevantPeopleRequest request) {
        this.request = request;
    }

    public void apply(RelevantPeopleEntity entity) {
        if (request == null || entity == null) return;

        entity.setCategory(request.getCategory());
        Name name = Name.of(request.getName());
        entity.setName(name != null ? name.getValue() : null);
        entity.setApparentAge(request.getApparentAge());
        entity.setCity(request.getCity());
        entity.setProfession(request.getProfession());
        entity.setBriefDescription(request.getBriefDescription());
    }
}
