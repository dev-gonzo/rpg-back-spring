package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Attribute {

    private final int value;

    private Attribute(int value) {
        if (value < 0) {
            throw new DomainException("Attribute value must be zero or positive.");
        }
        this.value = value;
    }

    public static Attribute of(int value) {
        return new Attribute(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
