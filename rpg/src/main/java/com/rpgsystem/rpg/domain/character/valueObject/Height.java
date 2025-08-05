package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Height {

    private final int centimeters;

    private Height(int centimeters) {
        if (centimeters <= 0) {
            throw new DomainException("Height must be greater than 0 cm.");
        }
        this.centimeters = centimeters;
    }

    public static Height of(Integer centimeters) {
        if (centimeters == null) return null;
        return new Height(centimeters);
    }

    public static Height from(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return new Height(Integer.parseInt(value));
        } catch (NumberFormatException ex) {
            throw new DomainException("Height must be a valid number.");
        }
    }
}
