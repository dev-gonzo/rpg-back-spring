package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.common.CodigoId;
import com.rpgsystem.rpg.domain.common.Name;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class CharacterEquipment {

    private final CodigoId id;
    private final CodigoId characterId;

    private final Name name;
    private final Integer quantity;
    private final String classification;
    private final String description;

    private final Integer kineticProtection;
    private final Integer ballisticProtection;
    private final Integer dexterityPenalty;
    private final Integer agilityPenalty;
    private final Integer initiative;
    private final String bookPage;

    private final LocalDate createdAt;
    private final LocalDate updatedAt;
}
