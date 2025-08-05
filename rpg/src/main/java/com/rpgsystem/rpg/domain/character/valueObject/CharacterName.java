package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class CharacterName {

    private final String value;

    public CharacterName(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException("Character name must not be null or blank");
        }

        if (value.length() > 100) {
            throw new DomainException("Character name must be at most 100 characters");
        }

        this.value = value.trim();
    }

    public static CharacterName of(String value) {
        if (value == null || value.isBlank()) return null;
        return new CharacterName(value);
    }

}
