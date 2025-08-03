package com.rpgsystem.rpg.domain.character.valueObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Modifier {

    private final int value;

    private Modifier(int value) {
        this.value = value;
    }

    public static Modifier of(Integer value) {
        if (value == null) return null;
        return new Modifier(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
