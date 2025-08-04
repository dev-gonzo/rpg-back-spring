package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.common.CodigoId;
import com.rpgsystem.rpg.domain.common.Name;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class CharacterWeapon {

    private final CodigoId id;
    private final CodigoId characterId;

    private final Name name;
    private final String description;
    private final String damage;
    private final Integer initiative;
    private final String range;
    private final String rof;
    private final String ammunition;
    private final String bookPage;

    private final LocalDate createdAt;
    private final LocalDate updatedAt;
}
