package com.rpgsystem.rpg.domain.character;

import com.rpgsystem.rpg.domain.common.CodigoId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Character {
    private final CodigoId id;
    private final CharacterCharacterInfo characterCharacterInfo;
}
