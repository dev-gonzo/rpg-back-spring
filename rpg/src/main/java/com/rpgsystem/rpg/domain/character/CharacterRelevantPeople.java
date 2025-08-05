package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.common.CodigoId;
import com.rpgsystem.rpg.domain.common.Name;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class CharacterRelevantPeople {

    private final CodigoId id;
    private final CodigoId characterId;

    private final String category;
    private final Name name;
    private final Integer apparentAge;
    private final String city;
    private final String profession;
    private final String briefDescription;

    private final LocalDate createdAt;
    private final LocalDate updatedAt;
}
