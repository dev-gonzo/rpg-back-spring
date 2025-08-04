package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.common.CodigoId;
import com.rpgsystem.rpg.domain.common.Name;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class CharacterRitualPower {

    private final CodigoId id;
    private final CodigoId characterId;

    private final Name name;
    private final String pathsForms;
    private final String description;
    private final String bookPage;

    private final LocalDate createdAt;
    private final LocalDate updatedAt;
}
