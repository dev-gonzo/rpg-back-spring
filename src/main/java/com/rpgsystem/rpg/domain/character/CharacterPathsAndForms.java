package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.character.valueObject.Focus;
import com.rpgsystem.rpg.domain.common.CodigoId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class CharacterPathsAndForms {

    private final CodigoId characterId;

    private final Focus understandForm;
    private final Focus createForm;
    private final Focus controlForm;

    private final Focus fire;
    private final Focus water;
    private final Focus earth;
    private final Focus air;
    private final Focus light;
    private final Focus darkness;
    private final Focus plants;
    private final Focus animals;
    private final Focus humans;
    private final Focus spiritum;
    private final Focus arkanun;
    private final Focus metamagic;

    private final LocalDate createdAt;
    private final LocalDate updatedAt;
}
