package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Focus {

    private final int value;

    private Focus(int value) {
        this.value = value;
    }

    public static Focus ofPath(Integer value) {
        if (value == null) return null;
        validate(value, 4, "Path");
        return new Focus(value);
    }

    public static Focus ofForm(Integer value) {
        if (value == null) return null;
        validate(value, 10, "Form");
        return new Focus(value);
    }

    private static void validate(int value, int max, String type) {
        if (value < 0) {
            throw new DomainException(type + " focus must not be negative.");
        }
        if (value > max) {
            throw new DomainException(type + " focus cannot be greater than " + max + ".");
        }
    }
}
