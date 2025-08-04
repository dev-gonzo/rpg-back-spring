package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.PathsAndFormsResponse;
import com.rpgsystem.rpg.domain.entity.PathsAndFormsEntity;

public class PathsAndFormsDtoBuilder {

    public static PathsAndFormsResponse from(PathsAndFormsEntity entity) {
        return PathsAndFormsResponse.builder()
                .characterId(entity.getCharacter().getId())
                .understandForm(entity.getUnderstandForm())
                .createForm(entity.getCreateForm())
                .controlForm(entity.getControlForm())

                .fire(entity.getFire())
                .water(entity.getWater())
                .earth(entity.getEarth())
                .air(entity.getAir())
                .light(entity.getLight())
                .darkness(entity.getDarkness())
                .plants(entity.getPlants())
                .animals(entity.getAnimals())
                .humans(entity.getHumans())
                .spiritum(entity.getSpiritum())
                .arkanun(entity.getArkanun())
                .metamagic(entity.getMetamagic())

                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
