package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.common.CodigoId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class CharacterBackground {

    private final CodigoId id;
    private final CodigoId characterId;

    private final String title;
    private final String text;
    private final boolean isPublic;

    private final LocalDate createdAt;
    private final LocalDate updatedAt;
}
